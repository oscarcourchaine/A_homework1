import java.io.*;
import java.util.*;

public class CSVEnrollmentParser {

    /**
     * Takes in a CSV file of enrollees and separates them into files by
     * insurance company, deduping and sorting as necessary.
     *
     * @param sourceFile CSV file to parse
     * @param outputFilePath directory for generated p[;output files
     */
    public static void parseCSVEnrollment(String sourceFile, String outputFilePath) throws IOException {
        HashMap<String, HashMap<String, CSVEnrollmentRecord>> companies = new HashMap<>();

        BufferedReader inputFile = new BufferedReader(new FileReader(sourceFile));
        String line = inputFile.readLine();
        while (line != null) {
            String[] lineParts = line.split(",");
            if (lineParts.length != 4) {
                throw new IOException("invalid file format");
            }

            CSVEnrollmentRecord r = new CSVEnrollmentRecord(lineParts);
            if (companies.containsKey(r.company_name)) {
                // Only adds if the company already exists, user is not already present, or if
                // user is present, r has a higher version number
                if (!companies.get(r.company_name).containsKey(r.id) ||
                    companies.get(r.company_name).get(r.id).version <= r.version) {
                        companies.get(r.company_name).put(r.id, r);
                    }
            } else {
                companies.put(r.company_name, new HashMap<>());
                companies.get(r.company_name).put(r.id, r);
            }

            //
            line = inputFile.readLine();
        }

        // Write output file(s)
        for (Map.Entry<String, HashMap<String, CSVEnrollmentRecord>> company : companies.entrySet()) {
            BufferedWriter outputFile = new BufferedWriter(
                    new FileWriter(outputFilePath + company.getKey() + ".csv"));
            List<CSVEnrollmentRecord> values = new ArrayList<>(company.getValue().values());
            Collections.sort(values);

            for (CSVEnrollmentRecord r : values) {
                outputFile.append(r.export());
            }
            outputFile.close();
        }
    }


    /**
     * Class for storing enrollee information.
     */
    private static class CSVEnrollmentRecord implements Comparable {
        public String id;
        public String name;
        public int version;
        public String company_name;

        public CSVEnrollmentRecord(String[] lineParts) {
            this.id = lineParts[0];
            this.name = parseName(lineParts[1]);
            this.version = Integer.parseInt(lineParts[2]);
            this.company_name = lineParts[3];
        }

        private String parseName(String s) {
            String[] nameParts = s.split(" ");
            if (nameParts.length != 2) {
                System.out.println("input exception");
                return s;
            } else return nameParts[1] + " " + nameParts[0];
        }

        @Override
        public int compareTo(Object o) {
            CSVEnrollmentRecord r2 = (CSVEnrollmentRecord) o;
            return this.name.compareToIgnoreCase(r2.name);
        }

        public String export() {
            return String.format("%s,%s,%d\n", this.id, this.name, this.version);
        }
    }

    /**
     * Generates a test file for the CSV EnrollmentParser.
     * @param outputFilePath location of test file.
     * @param rows number of rows to generate
     */
    public static void generateTestFile(String outputFilePath, int rows) {
        String[] insurance_companies = {"CompanyA", "CompanyB", "CompanyC"};
        Random r = new Random();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath + "test1.csv"));
            for (int i = 1; i < rows; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(i);
                sb.append(",Jane Doe,");
                sb.append("0,");
                sb.append(insurance_companies[r.nextInt(3)]);
                sb.append("\n");

                String line = sb.toString();
                bw.append(line);
            }

            // Adds special cases
            bw.append("0,Jane Doe,0,CompanyA\n");
            bw.append("0,Jane Doe,1,CompanyA\n");
            bw.append(String.valueOf(rows + 1)).append(",John Doe,0,CompanyA\n");
            bw.append(String.valueOf(rows + 2)).append(",Jane Fern,0,CompanyA\n");
            bw.append(String.valueOf(rows + 3)).append(",Jane Burns,0,CompanyA\n");
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
