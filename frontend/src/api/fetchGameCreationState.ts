import axios from "axios";
import { BACKEND_URL } from "../util/backendUrl";

export const fetchGameCreationState = async (bearerToken: string) => {
  try {
    const response = await axios.get(`${BACKEND_URL}/game/state`, {
      headers: { Authorization: `Bearer ${bearerToken}` },
    });
    return response.data;
  } catch (error) {
    console.log(error);
  }
};
