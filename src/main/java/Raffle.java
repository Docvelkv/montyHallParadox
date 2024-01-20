import java.util.*;

public class Raffle {
    private final Map<Integer, String> LOTS;
    private final String WIN_LOT = "автомобиль";
    private final Random RAND = new Random();

    /**
     * Конструктор
     */
    public Raffle(){
        this.LOTS = new HashMap<>();
        int keyWinLot = RAND.nextInt(1, 4);
        for(int i = 1; i < 4; i++){
            if(i == keyWinLot){
                LOTS.put(i, WIN_LOT);
            }else LOTS.put(i, "коза");
        }
    }

    /**
     * Получение лотов
     * @return Map<Integer, String>
     */
    private Map<Integer, String> getLOTS() {
        return LOTS;
    }

    /**
     * Проведение розыгрыша
     * @param changingSelection Изменение первоначального выбора
     * true - смена первоначального выбора
     * false - без изменения
     * @return String
     */
    private String conductingRaffle(boolean changingSelection){
        int playerChoice = RAND.nextInt(1, 4);
        List<Integer> list = new ArrayList<>();

        // Формирование списка пустых лотов (может содержать 1 или 2 варианта)
        for (Map.Entry<Integer, String> entry : getLOTS().entrySet()){
            if(entry.getKey() != playerChoice && !entry.getValue().equals(WIN_LOT)) {
                list.add(entry.getKey());
            }
        }

        // Удаление ведущим из розыгрыша одного пустого лота
        if (list.size() == 2) {
            int lotToDelete = RAND.nextInt(2);
            getLOTS().remove(list.get(lotToDelete));
        } else getLOTS().remove(list.getFirst());

        // Изменение игроком начального выбора лота
        if(changingSelection) {
            for (Map.Entry<Integer, String> entry : getLOTS().entrySet()) {
                if (entry.getKey() != playerChoice) {
                    playerChoice = entry.getKey();
                    break;
                }
            }
        }

        // Вывод результата розыгрыша
        if(getLOTS().get(playerChoice).equals(WIN_LOT)) return "Победа";
        return "Проигрыш";
    }

    /**
     * Список результатов розыгрышей в формате (№: результат)
     * @param numOfRaffles количество розыгрышей
     * @param mode режим розыгрыша
     * @return Map<Integer, String>
     */
    private static Map<Integer, String> listOfResults(int numOfRaffles, boolean mode){
        Map<Integer, String> list = new HashMap<>();
        String result;
        for(int i = 1; i <= numOfRaffles; i++){
            Raffle raffle = new Raffle();
            if(mode){
                result = raffle.conductingRaffle(true);
                list.put(i, result);
            }else {
                result = raffle.conductingRaffle(false);
                list.put(i, result);
            }
        }
        return list;
    }

    /**
     * Вывод статистики по победам и поражениям
     * @param numOfRaffles количество розыгрышей
     * @param mode режим розыгрыша
     */
    public static void showStatistics(int numOfRaffles, boolean mode){
        Map<Integer, String> map = listOfResults(numOfRaffles, mode);
        List<String> list = new ArrayList<>();
        for(Map.Entry<Integer, String> entry : map.entrySet()){
            list.add(entry.getValue());
        }
        double result = (double) Collections.frequency(list, "Победа") / numOfRaffles * 100;
        if(mode) {
            System.out.printf("""
                    %d розыгрышей с изменением первоначального выбора
                    Победы - %.1f%%
                    Поражения - %.1f%%%n
                    """, numOfRaffles, result, 100 - result);
        } else {
            System.out.printf("""
                    %d розыгрышей без изменения первоначального выбора
                    Победы - %.1f%%
                    Поражения - %.1f%%%n
                    """, numOfRaffles, result, 100 - result);
        }
    }
}