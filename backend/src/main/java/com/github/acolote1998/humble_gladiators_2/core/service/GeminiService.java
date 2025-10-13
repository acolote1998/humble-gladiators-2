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

import static com.github.acolote1998.humble_gladiators_2.core.util.PromptAider.*;

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
                
                %s
                
                The object structure context is: \n%s
                
                The "Requirement" structure is: \n%s
                
                The "RequirementEntry" structure is: \n%s
                
                The ArmorCategory values are: \n%s
                
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
                GetGeneralObjectGenerationRules());

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
            log.error("Could not map generated armors to ItemFromGeminiDto " + e.getMessage());
            e.printStackTrace();
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
                
                %s
                
                The object structure context is: \n%s
                
                The "Requirement" structure is: \n%s
                
                The "RequirementEntry" structure is: \n%s
                
                The BootsCategory values are: \n%s
                
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
                GetGeneralObjectGenerationRules());

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
            log.error("Could not map generated boots to ItemFromGeminiDto: " + e.getMessage());
            e.printStackTrace();
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
                
                %s
                
                The object structure context is: \n%s
                
                The "Requirement" structure is: \n%s
                
                The "RequirementEntry" structure is: \n%s
                
                The ConsumablesCategory values are: \n%s
                
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
                GetGeneralObjectGenerationRules());

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
            log.error("Could not map generated consumables to ItemFromGeminiDto: " + e.getMessage());
            e.printStackTrace();
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
                
                %s
                
                The object structure context is: \n%s
                
                The "Requirement" structure is: \n%s
                
                The "RequirementEntry" structure is: \n%s
                
                The HelmetCategory values are: \n%s
                
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
                GetGeneralObjectGenerationRules());

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
            log.error("Could not map generated helmets to ItemFromGeminiDto: " + e.getMessage());
            e.printStackTrace();
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
                
                %s
                
                The object structure context is: \n%s
                
                The "Requirement" structure is: \n%s
                
                The "RequirementEntry" structure is: \n%s
                
                The ShieldCategory values are: \n%s
                
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
                GetGeneralObjectGenerationRules());

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
            log.error("Could not map generated shields to ItemFromGeminiDto: " + e.getMessage());
            e.printStackTrace();
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
                
                %s
                
                The object structure context is: \n%s
                
                The "Requirement" structure is: \n%s
                
                The "RequirementEntry" structure is: \n%s
                
                The SpellCategory values are: \n%s
                
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
                GetGeneralObjectGenerationRules());

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
            log.error("Could not map generated items to ItemFromGeminiDto: " + e.getMessage());
            e.printStackTrace();
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
                
                %s
                
                The object structure context is: \n%s
                
                The "Requirement" structure is: \n%s
                
                The "RequirementEntry" structure is: \n%s
                
                The WeaponCategory values are: \n%s
                
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
                GetGeneralObjectGenerationRules());

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
            log.error("Could not map generated weapons to ItemFromGeminiDto: " + e.getMessage());
            e.printStackTrace();
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
                
                %s
                
                The object structure context is: \n%s
                
                The CharacterCategory values are: \n%s
                
                The "Stats" structure is: \n%s
                
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
                GetGeneralObjectGenerationRules());

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

    public String getPositiveArmorPromptForRuneware(
            Campaign campaign,
            ArmorTemplate armorTemplate) {
        log.info("Trying to generate prompt for runeware to generate an image");
        String geminiAnswer = "";
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating an armor, render only the armor—no body, mannequin, or person wearing it unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Armor",
                campaign.getTheme().getWantedThemes().toString(),
                armorTemplate.getName(),
                armorTemplate.getDescription(),
                TierToContext(armorTemplate.getTier()),
                RarityToContext(armorTemplate.getRarity()),
                GetCardImageGenerationGeneralRules());
        try {
            geminiAnswer = callGemini(promptForGemini);
            log.info("Prompt for Runeware is ready");
        } catch (InterruptedException e) {
            log.error("Error generating prompt for runeware" + e.getMessage());
        }
        return cleanResponseToJson(geminiAnswer);
    }

    public String getPositiveBootsPromptForRuneware(
            Campaign campaign,
            BootsTemplate bootsTemplate) {
        log.info("Trying to generate prompt for runeware to generate an image");
        String geminiAnswer = "";
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating boots, render only the boots—no body, mannequin, or person wearing them unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Boots",
                campaign.getTheme().getWantedThemes().toString(),
                bootsTemplate.getName(),
                bootsTemplate.getDescription(),
                TierToContext(bootsTemplate.getTier()),
                RarityToContext(bootsTemplate.getRarity()),
                GetCardImageGenerationGeneralRules());
        try {
            geminiAnswer = callGemini(promptForGemini);
            log.info("Prompt for Runeware is ready");
        } catch (InterruptedException e) {
            log.error("Error generating prompt for runeware" + e.getMessage());
        }
        return cleanResponseToJson(geminiAnswer);
    }

    public String getPositiveConsumablesPromptForRuneware(
            Campaign campaign,
            ConsumableTemplate consumableTemplate) {
        log.info("Trying to generate prompt for runeware to generate an image");
        String geminiAnswer = "";
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating a consumable, render only the consumable item—no background characters or additional objects unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Consumable",
                campaign.getTheme().getWantedThemes().toString(),
                consumableTemplate.getName(),
                consumableTemplate.getDescription(),
                TierToContext(consumableTemplate.getTier()),
                RarityToContext(consumableTemplate.getRarity()),
                GetCardImageGenerationGeneralRules());
        try {
            geminiAnswer = callGemini(promptForGemini);
            log.info("Prompt for Runeware is ready");
        } catch (InterruptedException e) {
            log.error("Error generating prompt for runeware" + e.getMessage());
        }
        return cleanResponseToJson(geminiAnswer);
    }

    public String getPositiveHelmetPromptForRuneware(
            Campaign campaign,
            HelmetTemplate helmetTemplate) {
        log.info("Trying to generate prompt for runeware to generate an image");
        String geminiAnswer = "";
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating a helmet, render only the helmet—no body, mannequin, or person wearing it unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Helmet",
                campaign.getTheme().getWantedThemes().toString(),
                helmetTemplate.getName(),
                helmetTemplate.getDescription(),
                TierToContext(helmetTemplate.getTier()),
                RarityToContext(helmetTemplate.getRarity()),
                GetCardImageGenerationGeneralRules());
        try {
            geminiAnswer = callGemini(promptForGemini);
            log.info("Prompt for Runeware is ready");
        } catch (InterruptedException e) {
            log.error("Error generating prompt for runeware" + e.getMessage());
        }
        return cleanResponseToJson(geminiAnswer);
    }

    public String getPositiveShieldPromptForRuneware(
            Campaign campaign,
            ShieldTemplate shieldTemplate) {
        log.info("Trying to generate prompt for runeware to generate an image");
        String geminiAnswer = "";
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating a shield, render only the shield—no body, mannequin, or person holding it unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Shield",
                campaign.getTheme().getWantedThemes().toString(),
                shieldTemplate.getName(),
                shieldTemplate.getDescription(),
                TierToContext(shieldTemplate.getTier()),
                RarityToContext(shieldTemplate.getRarity()),
                GetCardImageGenerationGeneralRules());
        try {
            geminiAnswer = callGemini(promptForGemini);
            log.info("Prompt for Runeware is ready");
        } catch (InterruptedException e) {
            log.error("Error generating prompt for runeware" + e.getMessage());
        }
        return cleanResponseToJson(geminiAnswer);
    }

    public String getPositiveSpellPromptForRuneware(
            Campaign campaign,
            SpellTemplate spellTemplate) {
        log.info("Trying to generate prompt for runeware to generate an image");
        String geminiAnswer = "";
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - The spell category is: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating a spell, render the magical effect, energy, or manifestation—no background characters or additional objects unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Spell",
                spellTemplate.getCategory(),
                campaign.getTheme().getWantedThemes().toString(),
                spellTemplate.getName(),
                spellTemplate.getDescription(),
                TierToContext(spellTemplate.getTier()),
                RarityToContext(spellTemplate.getRarity()),
                GetCardImageGenerationGeneralRules());
        try {
            geminiAnswer = callGemini(promptForGemini);
            log.info("Prompt for Runeware is ready");
        } catch (InterruptedException e) {
            log.error("Error generating prompt for runeware" + e.getMessage());
        }
        return cleanResponseToJson(geminiAnswer);
    }

    public String getPositiveWeaponPromptForRuneware(
            Campaign campaign,
            WeaponTemplate weaponTemplate) {
        log.info("Trying to generate prompt for runeware to generate an image");
        String geminiAnswer = "";
        String promptForGemini = String.format("""
                        You have to generate a prompt that will be sent to an AI that will generate high-quality fantasy artwork for a trading card game.
                        For generating the prompt, use this context:
                        You are generating high-quality fantasy artwork for a trading card in an RPG game.
                        - The object to illustrate is of type: %s
                        - The weapon category is: %s
                        - Focus strictly on the requested subject. Do not include any additional or implied elements unless explicitly specified \s
                        (e.g., if illustrating a weapon, render only the weapon—no body, mannequin, or person holding it unless instructed or included \s
                        in the object name or description).
                        - The card belongs to the campaign theme: %s.
                        - The object to illustrate is: "%s".
                        - A description of the object (for extra context): "%s"
                        - Details needed: %s
                        - Details needed: %s
                        %s
                        """,
                "Weapon",
                weaponTemplate.getCategory(),
                campaign.getTheme().getWantedThemes().toString(),
                weaponTemplate.getName(),
                weaponTemplate.getDescription(),
                TierToContext(weaponTemplate.getTier()),
                RarityToContext(weaponTemplate.getRarity()),
                GetCardImageGenerationGeneralRules());
        try {
            geminiAnswer = callGemini(promptForGemini);
            log.info("Prompt for Runeware is ready");
        } catch (InterruptedException e) {
            log.error("Error generating prompt for runeware" + e.getMessage());
        }
        return cleanResponseToJson(geminiAnswer);
    }
}
