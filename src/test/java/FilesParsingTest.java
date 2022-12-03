import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import model.FamilyJsonParse;

import static org.assertj.core.api.Assertions.assertThat;


public class FilesParsingTest {

    static ClassLoader cl = FilesParsingTest.class.getClassLoader();

    @Test
    public void zipParseTestForPDF() throws Exception {
        {
            try (
                    InputStream resources = cl.getResourceAsStream("example/Archive.zip");
                    ZipInputStream zis = new ZipInputStream(resources)
            ) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().contains("samplePDF.pdf")) {
                        PDF contentPDF = new PDF(zis);
                        assertThat(contentPDF.text).contains("A Simple PDF Fi");
                    } else if (entry.getName().contains("sampleXLSX.xlsx")) {
                        XLS contentXLS = new XLS(zis);
                        assertThat(contentXLS.excel.getSheetAt(0).getRow(2).getCell(1).getStringCellValue()).contains("Mara");
                    } else if (entry.getName().contains("addresses.csv")) {
                        CSVReader reader = new CSVReader(new InputStreamReader(zis));
                        List<String[]> contentCSV = reader.readAll();
                        assertThat(contentCSV.get(1)[0]).contains("Jack");
                    }

                }

            }
        }
    }

    @Test
    void jsonParseTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try (
                InputStream resources = cl.getResourceAsStream("example/family.json");
        ) {
            FamilyJsonParse familyJsonParse = objectMapper.readValue(resources, FamilyJsonParse.class);
            assertThat(familyJsonParse.family).isEqualTo("Smith");
            assertThat(familyJsonParse.id).isEqualTo(1234);
            assertThat(familyJsonParse.status).isTrue();
            assertThat(familyJsonParse.composition.get(0).name).isEqualTo("Adam");
            assertThat(familyJsonParse.composition.get(2).age).isEqualTo(17);
        }
    }
}


