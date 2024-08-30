import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, Integer> appsPerCategory = new HashMap<>();
        Map<String, Integer> companiesWithTheMostApps = new HashMap<>();
        Map<String, Integer> developersWithTheMostApps = new HashMap<>();
        Map<String, Double> pricePerApp = new HashMap<>();
        Map<String, Long> totalNumberOfDownloads = new HashMap<>();

        File f = new File("Google Play Store Apps.csv");

        Scanner s = new Scanner(f);
        s.nextLine();
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

                String companyKey = utils.getCompanyKey(appID);

                companiesWithTheMostApps.put(companyKey, companiesWithTheMostApps.getOrDefault(companyKey, 0) + 1);

                String companyFromEmail = utils.getCompanyFromEmail(developerEmail);

                if (!companyFromEmail.equalsIgnoreCase(companyKey)){
                    developersWithTheMostApps.put(
                            developerKey, developersWithTheMostApps.getOrDefault(developerKey,0) + 1
                    );
                }

                pricePerApp.put(appID, price);

                totalNumberOfDownloads.put(free, totalNumberOfDownloads.getOrDefault(free, 0L) + downloads);

            } catch (Exception e) {
                System.out.println("Error processing line: " + line);
                e.printStackTrace();
            }
        }
        saveToReport1(appsPerCategory);
        saveToReport2(companiesWithTheMostApps);
        saveToReport3(developersWithTheMostApps);
        saveToReport4(pricePerApp);
        saveToReport5(totalNumberOfDownloads);
    }

    private static void saveToReport5(Map<String, Long> totalNumberOfDownloads) throws IOException {
        FileWriter fw = new FileWriter("reports/download_statistics.csv");
        fw.write("Free, number of downloads\n");
        for (Map.Entry<String, Long> entry : totalNumberOfDownloads.entrySet()) {
            fw.write(entry.getKey() + ", " + entry.getValue() + "\n");
        }

        fw.close();

    }

    private static void saveToReport4(Map<String, Double> pricePerApp) throws IOException {
        List<Map.Entry<String, Double>> entryList = new ArrayList<>(pricePerApp.entrySet());
        entryList.sort((entry1, entry2) -> (entry2.getValue().compareTo(entry1.getValue())));
        FileWriter fw = new FileWriter("reports/app_purchasing_power.csv");

        Double value = 1000.0;
        int count = 0;
        for (Map.Entry<String, Double> entry : entryList) {
            if (entry.getValue() <= value) {
                value -= entry.getValue();
                count++;
            }
        }
        fw.write("We can buy " + count + " apps with $1000.\n\n");

        value = 10000.0;
        count = 0;
        for (Map.Entry<String, Double> entry : entryList) {
            if (entry.getValue() <= value) {
                value -= entry.getValue();
                count++;
            }
        }
        fw.write("We can buy " + count + " apps with $10000.\n\n");

        fw.close();
    }

    private static void saveToReport3(Map<String, Integer> developersWithTheMostApps) throws IOException {
        FileWriter fw = new FileWriter("reports/independent_developers.csv");
        fw.write("developer, number of apps \n");
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(developersWithTheMostApps.entrySet());
        entryList.sort((entry1, entry2) -> (entry2.getValue().compareTo(entry1.getValue())));

        for (int i = 0; i < 3; i++){
            Map.Entry<String, Integer> entry = entryList.get(i);
            fw.write(entry.getKey() + ", " + entry.getValue() + "\n");
        }
        fw.close();
    }

    private static void saveToReport2(Map<String, Integer> companiesWithTheMostApps) throws IOException {
        FileWriter fw = new FileWriter("reports/top_app_publishers.csv");
        fw.write("company, number of apps \n");

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(companiesWithTheMostApps.entrySet());
        entryList.sort((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()));

        for (int i = 0; i < Math.min(entryList.size(), 100); i++) {
            Map.Entry<String, Integer> entry = entryList.get(i);
            fw.write(entry.getKey() + ", " + entry.getValue() + "\n");
        }
        fw.close();
    }

    private static void saveToReport1(Map<String, Integer> appsPerCategory) throws IOException {
        FileWriter fw = new FileWriter("reports/category_distribution.csv");
        fw.write("category, number of apps\n");

        for (Map.Entry<String, Integer> entry : appsPerCategory.entrySet()) {
            fw.write(entry.getKey() + ", " + entry.getValue() + "\n");
        }
        fw.close();
    }
}