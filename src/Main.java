import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, Integer> appsPerCategory = new HashMap<>();
        Map<String, Integer> companiesWithTheMostApps = new HashMap<>();
        Map<String, Integer> developersWithTheMostApps = new HashMap<>();
        ArrayList<Double> prices = new ArrayList<>();
        Map<String, Long> totalNumberOfDownloads = new HashMap<>();

        File f = new File("Google Play Store Apps.csv");

        Scanner s = new Scanner(f);
        s.nextLine();
        ArrayList<String> badLines = new ArrayList<>();
        while (s.hasNextLine()) {
            String line = s.nextLine();
            try {
                String[] lineParts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                String categoryKey = lineParts[2].trim();
                String appID = lineParts[1].trim();
                String developerKey = lineParts[13].trim();
                String developerEmail = lineParts[15].trim();
                Double price = Double.parseDouble(lineParts[9].trim());
                String free = lineParts[8].trim();
                Long downloads = Long.parseLong(lineParts[5].trim().replaceAll("[^0-9]", ""));

                appsPerCategory.put(categoryKey, appsPerCategory.getOrDefault(categoryKey, 0) + 1);

                String companyKey = getCompanyKey(appID);

                companiesWithTheMostApps.put(companyKey, companiesWithTheMostApps.getOrDefault(companyKey, 0) + 1);

                String companyFromEmail = getCompanyFromEmail(developerEmail);

                if (!companyFromEmail.equalsIgnoreCase(companyKey)){
                    developersWithTheMostApps.put(
                            developerKey, developersWithTheMostApps.getOrDefault(developerKey,0) + 1
                    );
                }

                prices.add(price);

                totalNumberOfDownloads.put(free, totalNumberOfDownloads.getOrDefault(free, 0L) + downloads);

            } catch (Exception e) {
                System.out.println("Error processing line: " + line);
                e.printStackTrace();
                badLines.add(line);
            }
        }
        saveToReport1(appsPerCategory);
        saveToReport2(companiesWithTheMostApps);
        saveToReport3(developersWithTheMostApps);
        saveToReport4(prices);
        saveToReport5(totalNumberOfDownloads);
        saveToBadLines(badLines);



    }

    private static void saveToReport5(Map<String, Long> totalNumberOfDownloads) throws IOException {
        FileWriter fw = new FileWriter("Report 5.csv");
        fw.write("Free, number of downloads\n");
        for (Map.Entry<String, Long> entry : totalNumberOfDownloads.entrySet()) {
            fw.write(entry.getKey() + ", " + entry.getValue() + "\n");
        }

        fw.close();

    }

    private static void saveToReport4(ArrayList<Double> prices) throws IOException {
        prices.sort((price1, price2) -> (price2.compareTo(price1)));
        FileWriter fw = new FileWriter("Report 4.csv");
        double value1 = 1000.0, value2 = 10000.0;
        int count1 = 0, count2 = 0;
        for (Double price : prices) {
            if (price <= value1) {
                count1++;
                value1 -= price;
            }
            if (price <= value2) {
                count2++;
                value2 -= price;
            }
        }
        fw.write("We can buy " + count1 + " apps with " + "$1000. \n");
        fw.write("We can buy " + count2 + " apps with " + "$10000. \n");
        fw.close();
    }



    private static void saveToReport3(Map<String, Integer> developersWithTheMostApps) throws IOException {
        FileWriter fw = new FileWriter("Report 3.csv");
        fw.write("developer, number of apps \n");
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(developersWithTheMostApps.entrySet());
        entryList.sort((entry1, entry2) -> (entry2.getValue().compareTo(entry1.getValue())));

        for (int i = 0; i < 3; i++){
            Map.Entry<String, Integer> entry = entryList.get(i);
            fw.write(entry.getKey() + ", " + entry.getValue() + "\n");
        }
        fw.close();
    }

    private static String getCompanyFromEmail(String developerEmail) {
        int firstIndex = developerEmail.indexOf("@");

        String email = developerEmail.substring(firstIndex + 1);
        String[] emailParts = email.split("\\.");
        String lastWord = emailParts[emailParts.length - 1];
        String secondLastWord = emailParts[emailParts.length - 2];

        return lastWord + "." + secondLastWord;
    }

    private static void saveToReport2(Map<String, Integer> companiesWithTheMostApps) throws IOException {
        FileWriter fw = new FileWriter("Report 2.csv");
        fw.write("company, number of apps \n");

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(companiesWithTheMostApps.entrySet());
        entryList.sort((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()));

        for (int i = 0; i < Math.min(entryList.size(), 100); i++) {
            Map.Entry<String, Integer> entry = entryList.get(i);
            fw.write(entry.getKey() + ", " + entry.getValue() + "\n");
        }
        fw.close();
    }

    private static String getCompanyKey(String appID) {
        String[] appParts = appID.split("\\.");
        String firstWord = appParts[0].trim();
        String secondWord = appParts[1].trim();

        return (firstWord + "." + secondWord);
    }

    private static void saveToBadLines(ArrayList<String> badLines) throws IOException {
        FileWriter fw = new FileWriter("Bad Lines.csv");
        for (String line : badLines){
            fw.write(line + "\n");
        }
        fw.close();
    }


    private static void saveToReport1(Map<String, Integer> appsPerCategory) throws IOException {
        FileWriter fw = new FileWriter("Report 1.csv");
        fw.write("category, number of apps\n");

        for (Map.Entry<String, Integer> entry : appsPerCategory.entrySet()) {
            fw.write(entry.getKey() + ", " + entry.getValue() + "\n");
        }
        fw.close();
    }

}




