import java.util.List;

public class ConsoleView {

    public static void warningInputMsg(){
        System.out.println("명령어 형식이 올바르지 않습니다.");
        System.out.println("usage : [열][행]->[열][행] 또는 ?[열][행] 또는 exit");
        System.out.println("ex : A1->A2, ?A1, exit");
    }

    public static void failMove(){
        System.out.println("이동할 수 없습니다.");
    }

    public static void emptyPossiblePositions(){
        System.out.println("없음");
    }

    public static void possiblePositions(List<Position> positions) {
        positions.forEach(p->System.out.print(p + " "));
        System.out.println();
    }

    public static void warningColorMsg(Player player) {
        if(player.isWhite()){
            System.out.println("선택하신 기물은 백색이 아닙니다.\n");
            return;
        }
        System.out.println("선택하신 기물은 흑색이 아닙니다.\n");
    }

    public static void inputMsg(){
        System.out.print("명령을 입력하세요> ");
    }

    public static void turnMsg(Player player){
        String color = player.isWhite() ? "백색" : "흑색";
        System.out.printf("%s 체스말의 차례입니다.%n", color);
    }
    
    public static void initMsg(){
        System.out.println("(프로그램 실행)");
        System.out.println("체스 보드를 초기화했습니다.");
        System.out.println();
    }

    public static void display(Ranks ranks){
        System.out.println("  A B C D E F G H");
        for(int i = 1; i <= 8; i++){
            List<Piece> pieces = ranks.get(i);
            char[] chars = PieceFormatter.convertPieceToUnicode(pieces);
            System.out.print(i + " ");
            for(char c : chars){
                System.out.print(c + " ");
            }
            System.out.println();
        }
        System.out.println("  A B C D E F G H");
        System.out.println();
    }

    public static void playerWinMsg(Player player) {
        String color = player.isWhite() ? "백색" : "흑색";
        System.out.printf("%s 플레이어 승리!", color);
    }

    public static void displayScore(int whiteScore, int blackScore) {
        System.out.printf("백색 : %d%n", whiteScore);
        System.out.printf("흑색 : %d%n", blackScore);
        System.out.println();
    }
}
