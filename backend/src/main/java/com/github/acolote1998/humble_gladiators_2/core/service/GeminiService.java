package com.github.acolote1998.humble_gladiators_2.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.acolote1998.humble_gladiators_2.characters.enums.CharacterCategory;
import com.github.acolote1998.humble_gladiators_2.characters.model.CharacterInstance;
import com.github.acolote1998.humble_gladiators_2.characters.model.Stats;
import com.github.acolote1998.humble_gladiators_2.core.dto.CharacterFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.dto.GeminiResponseDto;
import com.github.acolote1998.humble_gladiators_2.core.dto.ItemFromGeminiDto;
import com.github.acolote1998.humble_gladiators_2.core.model.Campaign;
import com.github.acolote1998.humble_gladiators_2.core.model.Requirement;
import com.github.acolote1998.humble_gladiators_2.core.model.RequirementEntry;
import com.github.acolote1998.humble_gladiators_2.item.enums.ArmorCategory;
import com.github.acolote1998.humble_gladiators_2.item.enums.BootsCategory;
import com.github.acolote1998.humble_gladiators_2.item.enums.ConsumablesCategory;
import com.github.acolote1998.humble_gladiators_2.item.enums.HelmetCategory;
import com.github.acolote1998.humble_gladiators_2.item.enums.ShieldCategory;
import com.github.acolote1998.humble_gladiators_2.item.enums.SpellCategory;
import com.github.acolote1998.humble_gladiators_2.item.enums.WeaponCategory;
import com.github.acolote1998.humble_gladiators_2.item.templates.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class GeminiService {
    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    ObjectMapper mapper;

    // Gemini API endpoint for content generation
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";


    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public GeminiService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    private HttpEntity<Map<String, Object>> produceEntity(String prompt) {
        // Prepare the request body according to Gemini API specification
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    private String getFullUrl() {
        // Construct the full URL with API key
        return URL + "?key=" + apiKey;
    }

    private String cleanResponseToJson(String response) {
        return response.replaceAll("`", "").replaceAll("json", "");
    }

    private String callGemini(String prompt) throws InterruptedException {
        try {
            ResponseEntity<GeminiResponseDto> response = restTemplate.exchange(getFullUrl(), HttpMethod.POST, produceEntity(prompt), GeminiResponseDto.class);
            String resultText = Objects.requireNonNull(response.getBody())
                    .candidates().get(0)
                    .content().parts().get(0)
                    .text();
            return resultText;
        } catch (Exception e) {
            log.error("RETRYING. Error: " + e.getMessage());
            Thread.sleep(1000); //Waiting 1 sec before retrying
            return callGemini(prompt);
//            return "Error: Failed to communicate with Gemini API - " + e.getMessage();
        }
    }

    private String getGeneralGenerationRules() {
        return """
                     - Answer with ONLY json format, not extra text or explanations.
                     - Do not include "id", "createdAt", or "updatedAt" in the JSON.
                     - If a field represents an enum (like "requirementType"), it MUST be exactly one of the allowed provided values.
                     - Do NOT invent any new enum values. Only use the ones listed above.
                     - Do NOT generate item names or descriptions that imply in-game effects or powers. For example, avoid names like "Teleportation Boots" or descriptions like "This item gives the user the power of X".
                     - When generating text (e.g., names, descriptions), use the language appropriate to the theme. For example, if the theme is "Pirata", a weapon name could be "Espada" instead of "Sword".
                     - If the theme does not specify or imply a particular language (e.g., "Harry Potter" is language neutral, since it's just a person's name), always generate text in English.
                     - When generating names for characters or items:
                         - Prefer short names by default, like "Karen Filippelli" or "Thor".
                         - Only add extra descriptors if they make the character/item funnier, more memorable, or interesting in the context of the game.
                         - Avoid unnecessarily long names that include multiple descriptors without added value.
                     - When generating names or descriptions, you may mix elements from multiple wanted themes creatively, but only do so if this enhances the flavor, or thematic interest.
                         Examples:
                         - "Gamora, Scranton Branch Manager" ✅ (Good outcome: funny, thematic from 'The Office' + 'Marvel')
                         - "Thor, the Fire-Breathing Bard" ✅ (Good outcome: fun, thematic from 'Marvel' + 'Medieval Fantasy' + 'Music')
                         - "Karen Filippelli" ✅ (Good outcome: short, thematic from 'The Office', no need for extra context)
                         - "Sword of Messi" ✅ (Good outcome: thematic from 'Soccer' + 'Pirates')
                         - "Michael Scott, Regional Manager, That's What She Said" ❌ (Bad outcome: Too long, unnecessary)
                     - Do not force the generation to fit the category, if an object does not fit or does not make sense, just use "OTHER"
                """;
    }

    public String sendTestPrompt() throws InterruptedException {
        String prompt = "This is just a status check. If you are receiving this, answer with a flat string being 'Online: Gemini Controller is up'.";
        return callGemini(prompt);
    }

    public List<ItemFromGeminiDto> generateTwentyFiveArmors(Campaign campaign) {
        log.info("Trying to generate 25 armors through Gemini");
        Long campaignId = campaign.getId();
        String campaignTheme = campaign.getTheme().toString();
        String rawPrompt = """
                  You are generating data to create content for an RPG game.
                
                  Generate in json format an Array of 25 "%s".
                
                  The name, description have to be tailored to the theme context
                  - Create content following the wantedThemes
                  - Avoid following unwantedThemes
                
                  " %s "
                
                  The object structure context is: \n %s
                
                  The "Requirement" structure is: \n %s
                
                  The "RequirementEntry" structure is: \n %s
                
                  The ArmorCategory values are: \n %s
                
                 - Generate 1 object of each tier and each rarity. Example: {%s tier 1, rarity 1}, {%s tier 1 rarity 2}, etc.
                 - Not all generated objects need to have requirements, but it would make sense that some of them do, and the difficulty curve of the requirements should also make sense.
                 - If the generated object will not have a requirement, then make it null
                 - The only allowed object categories are things like: armors, robes, cloaks, capes, chestplates, breastplates and chest wear objects.
                 - Do not invent or include any other equipment types (for example helmets, gloves, shields).
                 %s
                """;

        String formattedPrompt = String.format(
                rawPrompt,
                "ArmorTemplate",
                campaignTheme,
                ArmorTemplate.ObjectStructure(campaignId),
                Requirement.RequirementStructure(campaignId),
                RequirementEntry.RequirementEntryStructure(campaignId),
                ArmorCategory.AllArmorCategoryToString(),
                "Armor",
                "Armor",
                getGeneralGenerationRules());

        String rawAnswer = "";
        try {
            rawAnswer = callGemini(formattedPrompt);
        } catch (InterruptedException e) {
            log.error("Error generating armors: " + e.getMessage());
        }
        String processedAnswer = cleanResponseToJson(rawAnswer);

        List<ItemFromGeminiDto> generatedArmors = new ArrayList<>();
        try {
            generatedArmors = mapper.readValue(processedAnswer, new TypeReference<List<ItemFromGeminiDto>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Could not map generated items to ItemFromGeminiDto");
        }
        return generatedArmors;
    }

    public List<ItemFromGeminiDto> generateTwentyFiveBoots(Campaign campaign) {
        log.info("Trying to generate 25 boots through Gemini");
        Long campaignId = campaign.getId();
        String campaignTheme = campaign.getTheme().toString();
        String rawPrompt = """
                  You are generating data to create content for an RPG game.
                
                  Generate in json format an Array of 25 "%s".
                
                  The name, description have to be tailored to the theme context
                  - Create content following the wantedThemes
                  - Avoid following unwantedThemes
                
                  " %s "
                
                  The object structure context is: \n %s
                
                  The "Requirement" structure is: \n %s
                
                  The "RequirementEntry" structure is: \n %s
                
                  The BootsCategory values are: \n %s
                
                 - Generate 1 object of each tier and each rarity. Example: {%s tier 1, rarity 1}, {%s tier 1 rarity 2}, etc.
                 - Not all generated objects need to have requirements, but it would make sense that some of them do, and the difficulty curve of the requirements should also make sense.
                 - If the generated object will not have a requirement, then make it null
                 %s
                """;

        String formattedPrompt = String.format(
                rawPrompt,
                "BootsTemplate",
                campaignTheme,
                BootsTemplate.ObjectStructure(campaignId),
                Requirement.RequirementStructure(campaignId),
                RequirementEntry.RequirementEntryStructure(campaignId),
                BootsCategory.AllBootsCategoryToString(),
                "Boot",
                "Boot",
                getGeneralGenerationRules());

        String rawAnswer = "";
        try {
            rawAnswer = callGemini(formattedPrompt);
        } catch (InterruptedException e) {
            log.error("Error generating boots: " + e.getMessage());
        }
        String processedAnswer = cleanResponseToJson(rawAnswer);

        List<ItemFromGeminiDto> generatedItems = new ArrayList<>();
        try {
            generatedItems = mapper.readValue(processedAnswer, new TypeReference<List<ItemFromGeminiDto>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Could not map generated items to ItemFromGeminiDto");
        }
        return generatedItems;
    }

    public List<ItemFromGeminiDto> generateTwentyFiveConsumables(Campaign campaign) {
        log.info("Trying to generate 25 consumables through Gemini");
        Long campaignId = campaign.getId();
        String campaignTheme = campaign.getTheme().toString();
        String rawPrompt = """
                  You are generating data to create content for an RPG game.
                
                  Generate in json format an Array of 25 "%s".
                
                  The name, description have to be tailored to the theme context
                  - Create content following the wantedThemes
                  - Avoid following unwantedThemes
                
                  " %s "
                
                  The object structure context is: \n %s
                
                  The "Requirement" structure is: \n %s
                
                  The "RequirementEntry" structure is: \n %s
                
                  The ConsumablesCategory values are: \n %s
                
                 - Generate 1 object of each tier and each rarity. Example: {%s tier 1, rarity 1}, {%s tier 1 rarity 2}, etc.
                 - Not all generated objects need to have requirements, but it would make sense that some of them do, and the difficulty curve of the requirements should also make sense.
                 - If the generated object will not have a requirement, then make it null
                 %s
                """;

        String formattedPrompt = String.format(
                rawPrompt,
                "ConsumableTemplate (for example, if the theme was magic, medieval, etc, then a consumable could be a potion)",
                campaignTheme,
                ConsumableTemplate.ObjectStructure(campaignId),
                Requirement.RequirementStructure(campaignId),
                RequirementEntry.RequirementEntryStructure(campaignId),
                ConsumablesCategory.AllConsumablesCategoryToString(),
                "Consumable",
                "Consumable",
                getGeneralGenerationRules());

        String rawAnswer = "";
        try {
            rawAnswer = callGemini(formattedPrompt);
        } catch (InterruptedException e) {
            log.error("Error generating consumables: " + e.getMessage());
        }
        String processedAnswer = cleanResponseToJson(rawAnswer);

        List<ItemFromGeminiDto> generatedItems = new ArrayList<>();
        try {
            generatedItems = mapper.readValue(processedAnswer, new TypeReference<List<ItemFromGeminiDto>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Could not map generated items to ItemFromGeminiDto");
        }
        return generatedItems;
    }

    public List<ItemFromGeminiDto> generateTwentyFiveHelmets(Campaign campaign) {
        log.info("Trying to generate 25 helmets through Gemini");
        Long campaignId = campaign.getId();
        String campaignTheme = campaign.getTheme().toString();
        String rawPrompt = """
                  You are generating data to create content for an RPG game.
                
                  Generate in json format an Array of 25 "%s".
                
                  The name, description have to be tailored to the theme context
                  - Create content following the wantedThemes
                  - Avoid following unwantedThemes
                
                  " %s "
                
                  The object structure context is: \n %s
                
                  The "Requirement" structure is: \n %s
                
                  The "RequirementEntry" structure is: \n %s
                
                  The HelmetCategory values are: \n %s
                
                 - Generate 1 object of each tier and each rarity. Example: {%s tier 1, rarity 1}, {%s tier 1 rarity 2}, etc.
                 - Not all generated objects need to have requirements, but it would make sense that some of them do, and the difficulty curve of the requirements should also make sense.
                 - If the generated object will not have a requirement, then make it null
                 %s
                """;

        String formattedPrompt = String.format(
                rawPrompt,
                "HelmetTemplate",
                campaignTheme,
                HelmetTemplate.ObjectStructure(campaignId),
                Requirement.RequirementStructure(campaignId),
                RequirementEntry.RequirementEntryStructure(campaignId),
                HelmetCategory.AllHelmetCategoryToString(),
                "Helmet",
                "Helmet",
                getGeneralGenerationRules());

        String rawAnswer = "";
        try {
            rawAnswer = callGemini(formattedPrompt);
        } catch (InterruptedException e) {
            log.error("Error generating helmets: " + e.getMessage());
        }
        String processedAnswer = cleanResponseToJson(rawAnswer);

        List<ItemFromGeminiDto> generatedItems = new ArrayList<>();
        try {
            generatedItems = mapper.readValue(processedAnswer, new TypeReference<List<ItemFromGeminiDto>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Could not map generated items to ItemFromGeminiDto");
        }
        return generatedItems;
    }

    public List<ItemFromGeminiDto> generateTwentyFiveShields(Campaign campaign) {
        log.info("Trying to generate 25 shields through Gemini");
        Long campaignId = campaign.getId();
        String campaignTheme = campaign.getTheme().toString();
        String rawPrompt = """
                  You are generating data to create content for an RPG game.
                
                  Generate in json format an Array of 25 "%s".
                
                  The name, description have to be tailored to the theme context
                  - Create content following the wantedThemes
                  - Avoid following unwantedThemes
                
                  " %s "
                
                  The object structure context is: \n %s
                
                  The "Requirement" structure is: \n %s
                
                  The "RequirementEntry" structure is: \n %s
                
                  The ShieldCategory values are: \n %s
                
                 - Generate 1 object of each tier and each rarity. Example: {%s tier 1, rarity 1}, {%s tier 1 rarity 2}, etc.
                 - Not all generated objects need to have requirements, but it would make sense that some of them do, and the difficulty curve of the requirements should also make sense.
                 - If the generated object will not have a requirement, then make it null
                 - You must always reinterpret "Shield" in the context of the campaign theme.
                 - A "Shield" does not always mean a physical shield.
                 - Instead, treat it as a right-hand defensive or thematic equipment item.
                 - For example: in a wizard theme it could be a spellbook, in a cleric theme a holy scripture, in a necromancer theme a bone totem.
                 - Every generated object must clearly fit both the theme and the concept of a "Shield" as a defensive or secondary item.
                 - Do NOT create objects within these equipment types: helmets, armors, boots, weapons.
                 %s
                """;

        String formattedPrompt = String.format(
                rawPrompt,
                "ShieldTemplate",
                campaignTheme,
                ShieldTemplate.ObjectStructure(campaignId),
                Requirement.RequirementStructure(campaignId),
                RequirementEntry.RequirementEntryStructure(campaignId),
                ShieldCategory.AllShieldCategoryToString(),
                "Shield",
                "Shield",
                getGeneralGenerationRules());

        String rawAnswer = "";
        try {
            rawAnswer = callGemini(formattedPrompt);
        } catch (InterruptedException e) {
            log.error("Error generating shields: " + e.getMessage());
        }
        String processedAnswer = cleanResponseToJson(rawAnswer);

        List<ItemFromGeminiDto> generatedItems = new ArrayList<>();
        try {
            generatedItems = mapper.readValue(processedAnswer, new TypeReference<List<ItemFromGeminiDto>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Could not map generated items to ItemFromGeminiDto");
        }
        return generatedItems;
    }

    public List<ItemFromGeminiDto> generateTwentyFiveSpells(Campaign campaign) {
        log.info("Trying to generate 25 spells through Gemini");
        Long campaignId = campaign.getId();
        String campaignTheme = campaign.getTheme().toString();
        String rawPrompt = """
                  You are generating data to create content for an RPG game.
                
                  Generate in json format an Array of 25 "%s".
                
                  The name, description have to be tailored to the theme context
                  - Create content following the wantedThemes
                  - Avoid following unwantedThemes
                
                  " %s "
                
                  The object structure context is: \n %s
                
                  The "Requirement" structure is: \n %s
                
                  The "RequirementEntry" structure is: \n %s
                
                  The SpellCategory values are: \n %s
                
                 - Generate 1 object of each tier and each rarity. Example: {%s tier 1, rarity 1}, {%s tier 1 rarity 2}, etc.
                 - Not all generated objects need to have requirements, but it would make sense that some of them do, and the difficulty curve of the requirements should also make sense.
                 - If the generated object will not have a requirement, then make it null
                 - All spells must have a RequirementEntry that forces the user to have certain minimum MP (magic points)
                    -Example: {requirementType: MP, operator: MOREOREQUALTHAN, value: "10"}
                    -The MP requirement needs to make sense and scale together with the spell tier and rarity
                 %s
                """;

        String formattedPrompt = String.format(
                rawPrompt,
                "SpellTemplate",
                campaignTheme,
                SpellTemplate.ObjectStructure(campaignId),
                Requirement.RequirementStructure(campaignId),
                RequirementEntry.RequirementEntryStructure(campaignId),
                SpellCategory.AllSpellCategoryToString(),
                "Spell",
                "Spell",
                getGeneralGenerationRules());

        String rawAnswer = "";
        try {
            rawAnswer = callGemini(formattedPrompt);
        } catch (InterruptedException e) {
            log.error("Error generating Spell: " + e.getMessage());
        }
        String processedAnswer = cleanResponseToJson(rawAnswer);

        List<ItemFromGeminiDto> generatedItems = new ArrayList<>();
        try {
            generatedItems = mapper.readValue(processedAnswer, new TypeReference<List<ItemFromGeminiDto>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Could not map generated items to ItemFromGeminiDto");
        }
        return generatedItems;
    }

    public List<ItemFromGeminiDto> generateTwentyFiveWeapons(Campaign campaign) {
        log.info("Trying to generate 25 weapons through Gemini");
        Long campaignId = campaign.getId();
        String campaignTheme = campaign.getTheme().toString();
        String rawPrompt = """
                  You are generating data to create content for an RPG game.
                
                  Generate in json format an Array of 25 "%s".
                
                  The name, description have to be tailored to the theme context
                  - Create content following the wantedThemes
                  - Avoid following unwantedThemes
                
                  " %s "
                
                  The object structure context is: \n %s
                
                  The "Requirement" structure is: \n %s
                
                  The "RequirementEntry" structure is: \n %s
                
                  The WeaponCategory values are: \n %s
                
                 - Generate 1 object of each tier and each rarity. Example: {%s tier 1, rarity 1}, {%s tier 1 rarity 2}, etc.
                 - Not all generated objects need to have requirements, but it would make sense that some of them do, and the difficulty curve of the requirements should also make sense.
                 - If the generated object will not have a requirement, then make it null
                 %s
                """;

        String formattedPrompt = String.format(
                rawPrompt,
                "WeaponTemplate",
                campaignTheme,
                WeaponTemplate.ObjectStructure(campaignId),
                Requirement.RequirementStructure(campaignId),
                RequirementEntry.RequirementEntryStructure(campaignId),
                WeaponCategory.AllWeaponCategoryToString(),
                "Weapon",
                "Weapon",
                getGeneralGenerationRules());

        String rawAnswer = "";
        try {
            rawAnswer = callGemini(formattedPrompt);
        } catch (InterruptedException e) {
            log.error("Error generating Weapon: " + e.getMessage());
        }
        String processedAnswer = cleanResponseToJson(rawAnswer);

        List<ItemFromGeminiDto> generatedItems = new ArrayList<>();
        try {
            generatedItems = mapper.readValue(processedAnswer, new TypeReference<List<ItemFromGeminiDto>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Could not map generated items to ItemFromGeminiDto");
        }
        return generatedItems;
    }

    public List<CharacterFromGeminiDto> generateTenNpcsOfDesiredTier(Campaign campaign,
                                                                     List<CharacterInstance> existingCharsForContext,
                                                                     Integer tierToGenerate) {
        log.info(String.format("Trying to generate 10 NPCs Tier %s through Gemini", tierToGenerate));
        Long campaignId = campaign.getId();
        String campaignTheme = campaign.getTheme().toString();
        String charsForContext = "";
        if (!existingCharsForContext.isEmpty()) {
            charsForContext = String.format("""
                    ** Just for context, this is a list of the already existing characters. Avoid creating the same ones again:  **
                    - List: %s
                    """, existingCharsForContext.toString());
        }
        String rawPrompt = """
                  You are generating data to create content for an RPG game.
                
                  Generate in json format an Array of 10 "%s".
                
                  The name, description have to be tailored to the theme context
                  - Create content following the wantedThemes
                  - Avoid following unwantedThemes
                
                  " %s "
                
                  The object structure context is: \n %s
                
                  The CharacterCategory values are: \n %s
                
                  The "Stats" structure is: \n %s
                
                  %s
                
                  - Do not force the generation to fit the CharacterCategory, if an object does not fit or does not make sense, just use "OTHER"
                  - Generate 2 NPCs of tier %s for each rarity level. Example: {NPC1 tier %s, rarity 1}, {NPC2 tier %s, rarity 1}, {NPC3 tier %s, rarity 2}, etc.
                  %s
                """;

        String formattedPrompt = String.format(
                rawPrompt,
                "'CharacterInstance' (NPCs - Tier " + tierToGenerate + ")",
                campaignTheme,
                CharacterInstance.ObjectStructure(campaignId),
                CharacterCategory.AllCharacterCategoryToString(),
                Stats.ObjectStructure(),
                charsForContext,
                tierToGenerate,
                tierToGenerate,
                tierToGenerate,
                tierToGenerate,
                getGeneralGenerationRules());

        String rawAnswer = "";
        try {
            rawAnswer = callGemini(formattedPrompt);
        } catch (InterruptedException e) {
            log.error("Error generating Char tier " + tierToGenerate + ": " + e.getMessage());
        }
        String processedAnswer = cleanResponseToJson(rawAnswer);

        List<CharacterFromGeminiDto> generatedCharacters = new ArrayList<>();
        try {
            generatedCharacters = mapper.readValue(processedAnswer, new TypeReference<List<CharacterFromGeminiDto>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Could not map generated characters to CharacterFromGeminiDto");
        }
        return generatedCharacters;
    }

}
