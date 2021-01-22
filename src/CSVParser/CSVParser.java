package CSVParser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CSVParser {
	//Основной метод приложения
    public static void main(String[] args) throws IOException {
        String filePath = "D:\\Программирование\\Eclipse Workspace\\UCSDUnfoldingMaps\\data\\USA_march_scheduled.txt";
        List<FlightModel> flights = ParseFlightsCsv(filePath);
        
        
        Set<String> airportsDB = new HashSet<String>();
        for (FlightModel s : flights) {
        	airportsDB.add(s.destAirportId);
        	airportsDB.add(s.originAirportId);
        }
        System.out.println(airportsDB.size());
        String t[] = "\"10370\",\"Ascension, Bolivia: Ascension Airport\"".split(",", 2);
        for (String s : t)
        	System.out.println(s);
        
        String filePath2 = "D:\\Программирование\\Eclipse Workspace\\UCSDUnfoldingMaps\\data\\airports_id.txt";
        String filePath3 = "D:\\Программирование\\Eclipse Workspace\\UCSDUnfoldingMaps\\data\\NEW_AIRPORTS_DB1.txt";
        String filePath4 = "D:\\Программирование\\Eclipse Workspace\\UCSDUnfoldingMaps\\data\\airports_id.txt";
        List<AirportModel> airports = ParseAirportsCsv(filePath4);
        System.out.println(airports.get(0));
        System.out.println(airports.get(1));
        
        /*PrintWriter s = new PrintWriter(new File("D:\\Программирование\\Eclipse Workspace\\UCSDUnfoldingMaps"
        		+ "\\data\\airports_db.txt"));
        for (String id : airportsDB) {
        	for (AirportModel m : airports) {
        		if (id.equals(m.id)) {
        			s.println(id + "," + m.name + "," + m.city);
        		}
        	}
        }
        s.close();*/
        /*int counter = 0;
        boolean find = false;
        for (FlightModel s : flights) {
        	String id = s.originAirportId;
        	for (AirportModel a : airports) {
        		if (id.equals(a.id)) {
        			find = true;
        			break;
        		}
        	}
        	if (find != true) {
        		counter++;
        	}
        	else find = false;
        }
        System.out.println(counter);*/
    }

	
	//Расинг CSV файла по указанному пути и получение данных из него
	public static List<FlightModel> ParseFlightsCsv(String filePath) throws IOException {
		//Загружаем строки из файла
		List<FlightModel> flights = new ArrayList<FlightModel>();
		List<String> fileLines = Files.readAllLines(Paths.get(filePath));
		for (String fileLine : fileLines) {
            String[] splitedText = fileLine.split(",");
            ArrayList<String> columnList = new ArrayList<String>();
            for (int i = 0; i < splitedText.length; i++) {
                //Если колонка начинается на кавычки или заканчиваеться на кавычки
                if (IsColumnPart(splitedText[i])) {
                    String lastText = columnList.get(columnList.size() - 1);
                    columnList.set(columnList.size() - 1, lastText + ","+ splitedText[i]);
                } else {
                    columnList.add(splitedText[i]);
                }
            }
            FlightModel model = new FlightModel();
            model.date = columnList.get(0);
            model.company = columnList.get(1);
            model.originAirportId = columnList.get(2);
            model.originCity = columnList.get(3);
            model.destAirportId = columnList.get(4);
            model.destCity = columnList.get(5);
            model.depTime = columnList.get(6);
            model.arrTime = columnList.get(7);
            flights.add(model);
		}
            
		return flights;
	}
	
	public static List<AirportModel> ParseAirportsCsv(String filePath) throws IOException {
		//Загружаем строки из файла
				List<AirportModel> airports = new ArrayList<AirportModel>();
				List<String> fileLines = Files.readAllLines(Paths.get(filePath));
				for (String fileLine : fileLines) {
					fileLine = fileLine.trim();
		            String[] splitedText = fileLine.split(",", 2);
		            
		            AirportModel model = new AirportModel();
		            // Убираем ковычки
		            model.id = splitedText[0].substring(1, splitedText[0].length() - 1);
		            // Убираем ковычки
		            String temp = splitedText[1].substring(1, splitedText[1].length() - 1);
		            //System.out.println(temp);
		            String ss[] = temp.split(":");
		            model.city = ss[0].replace(',', '-');
		            model.name = ss[1];
		            //model.name = columnList.get(1).substring(1, columnList.get(1).length() - 1).split(",")[0];
		            
		            /*model.id = columnList.get(0);
		            model.name = columnList.get(1);*/
		            airports.add(model);
				}
		            
				return airports;
	}
	
	//Проверка является ли колонка частью предыдущей колонки
    private static boolean IsColumnPart(String text) {
        String trimText = text.trim();
        //Если в тексте одна ковычка и текст на нее заканчиваеться значит это часть предыдущей колонки
        return trimText.indexOf("\"") == trimText.lastIndexOf("\"") && trimText.endsWith("\"");
    }

}
