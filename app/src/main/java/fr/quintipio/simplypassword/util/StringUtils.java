package fr.quintipio.simplypassword.util;

public class StringUtils {

	/**
	 * Vérifie si une chaine de caractère est vide
	 * @param chaine la chaine
	 * @return true si vide
	 */
	public static boolean stringEmpty(String chaine) {
		if(chaine == null) {
			return true;
		}
		else {
			return chaine.trim().isEmpty() || chaine.trim().length() == 0;
		}
	}
	

}
