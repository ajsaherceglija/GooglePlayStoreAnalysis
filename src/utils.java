public class utils {
    static String getCompanyFromEmail(String developerEmail) {
        int firstIndex = developerEmail.indexOf("@");

        String email = developerEmail.substring(firstIndex + 1);
        String[] emailParts = email.split("\\.");
        String lastWord = emailParts[emailParts.length - 1];
        String secondLastWord = emailParts[emailParts.length - 2];

        return lastWord + "." + secondLastWord;
    }

    static String getCompanyKey(String appID) {
        String[] appParts = appID.split("\\.");
        String firstWord = appParts[0].trim();
        String secondWord = appParts[1].trim();

        return (firstWord + "." + secondWord);
    }
}