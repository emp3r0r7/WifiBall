package org.emp3r0r7.filesystem;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.emp3r0r7.model.AccessPoint;
import org.emp3r0r7.model.Station;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.emp3r0r7.utils.StringUtil.safeParseInt;
import static org.emp3r0r7.utils.StringUtil.safeTrim;

public class CsvParser {

    private static final String DEVICE_DELIMITER = "Station MAC";

    public static List<AccessPoint> parseAccessPoints(String filePath) {
        List<AccessPoint> routers = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withSkipLines(1).build()) {
            String[] line;
            boolean firstNonEmptyLineSkipped = false;

            while ((line = reader.readNext()) != null) {
                // Interrompi la lettura se raggiungi la sezione delle Stations
                if (line[0].trim().equalsIgnoreCase(DEVICE_DELIMITER)) {
                    break;
                }

                // Ignora righe completamente vuote o con solo spazi
                if (Arrays.stream(line).allMatch(String::isEmpty)) {
                    continue;
                }

                // Salta la prima riga non vuota
                if (!firstNonEmptyLineSkipped && !line[0].trim().isEmpty()) {
                    firstNonEmptyLineSkipped = true;
                    continue;
                }

                // Controlla se la riga ha il numero corretto di campi, gestendo anche campi vuoti
                if (line.length >= 15) {
                    AccessPoint router = new AccessPoint();

                    router.setBssid(safeTrim(line[0]));
                    router.setFirstTimeSeen(safeTrim(line[1]));
                    router.setLastTimeSeen(safeTrim(line[2]));

                    router.setChannel(safeParseInt(line[3], 0)); // 0 come valore predefinito
                    router.setSpeed(safeParseInt(line[4], 0)); // 0 come valore predefinito

                    router.setPrivacy(safeTrim(line[5]));
                    router.setCipher(safeTrim(line[6]));
                    router.setAuthentication(safeTrim(line[7]));

                    router.setPower(safeParseInt(line[8], -100)); // -100 come valore predefinito
                    router.setBeacons(safeParseInt(line[9], 0)); // 0 come valore predefinito
                    router.setIv(safeParseInt(line[10], 0)); // 0 come valore predefinito

                    router.setLanIp(safeTrim(line[11]));

                    router.setIdLength(safeParseInt(line[12], 0)); // 0 come valore predefinito

                    router.setEssid(safeTrim(line[13]));
                    router.setKey(safeTrim(line[14]));

                    routers.add(router);
                } else {
                    System.out.println("Riga non valida (lunghezza " + line.length + "): " + Arrays.toString(line));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return routers;
    }


    public static List<Station> parseStations(String filePath) {
        List<Station> stations = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).withSkipLines(1).build()) {
            String[] line;
            boolean stationSectionStarted = false;

            while ((line = reader.readNext()) != null) {
                // Controlla se siamo nella sezione delle Stations
                if (!stationSectionStarted) {
                    if (line[0].trim().equalsIgnoreCase(DEVICE_DELIMITER)) {
                        stationSectionStarted = true; // Trova la sezione delle Stations
                        continue; // Salta l'header delle Stations
                    } else {
                        continue; // Continua a cercare la sezione delle Stations
                    }
                }

                // Ignora righe completamente vuote o con solo spazi
                if (Arrays.stream(line).allMatch(String::isEmpty)) {
                    continue;
                }

                // Controlla se la riga ha il numero corretto di campi, gestendo anche campi vuoti
                if (line.length >= 7) {
                    Station station = new Station();

                    station.setStationMac(safeTrim(line[0]));
                    station.setFirstTimeSeen(safeTrim(line[1]));
                    station.setLastTimeSeen(safeTrim(line[2]));

                    station.setPower(safeParseInt(line[3].trim(), -100)); // -100 come valore predefinito
                    station.setPackets(safeParseInt(line[4].trim(), 0)); // 0 come valore predefinito

                    station.setBssid(safeTrim(line[5]));
                    station.setProbedEssids(safeTrim(line[6]));

                    stations.add(station);
                } else {
                    System.out.println("Riga non valida (lunghezza " + line.length + "): " + Arrays.toString(line));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stations;
    }

}
