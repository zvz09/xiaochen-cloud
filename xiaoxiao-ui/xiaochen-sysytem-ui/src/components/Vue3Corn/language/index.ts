import en from "./en";
import cn from "./cn";
import pt from "./pt_br";
import { Translations } from "@/components/Vue3Corn/types";

const languageMap: Record<string, Translations> = {
  en,
  cn,
  pt
  // Add more languages here dynamically
};

export default languageMap;
