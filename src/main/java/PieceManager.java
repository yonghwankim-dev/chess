import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PieceManager {
    private static final int TOTAL_SCORE = 49;
    private List<Piece> pieces;

    public PieceManager() {
        this.pieces = new ArrayList<>();
    }

    public void clear(){
        pieces.clear();
    }

    public void init(){
        initPawn(Rank.SEVEN, Color.WHITE);
        initRook(Rank.EIGHT, Color.WHITE);
        initBishop(Rank.EIGHT, Color.WHITE);
        initQueen(Rank.EIGHT, Color.WHITE);
        initKnight(Rank.EIGHT, Color.WHITE);
        initKing(Rank.EIGHT, Color.WHITE);
        initPawn(Rank.TWO, Color.BLACK);
        initRook(Rank.ONE, Color.BLACK);
        initBishop(Rank.ONE, Color.BLACK);
        initQueen(Rank.ONE, Color.BLACK);
        initKnight(Rank.ONE, Color.BLACK);
        initKing(Rank.ONE, Color.BLACK);
        initEmpty();
    }

    private void initPawn(Rank rank, Color color){
        Arrays.stream(File.values())
                .filter(f->f != File.INVALID)
                .map(f -> PieceFactory.pawn(color, Position.create(f, rank)))
                .forEachOrdered(pieces::add);
    }

    private void initRook(Rank rank, Color color){
        pieces.add(PieceFactory.rook(color, Position.create(File.A, rank)));
        pieces.add(PieceFactory.rook(color, Position.create(File.H, rank)));
    }

    private void initBishop(Rank rank, Color color){
        pieces.add(PieceFactory.bishop(color, Position.create(File.C, rank)));
        pieces.add(PieceFactory.bishop(color, Position.create(File.F, rank)));
    }

    private void initQueen(Rank rank, Color color){
        pieces.add(PieceFactory.queen(color, Position.create(File.E, rank)));
    }

    private void initKnight(Rank rank, Color color){
        pieces.add(PieceFactory.knight(color, Position.create(File.B, rank)));
        pieces.add(PieceFactory.knight(color, Position.create(File.G, rank)));
    }

    private void initKing(Rank rank, Color color){
        pieces.add(PieceFactory.king(color, Position.create(File.D, rank)));
    }

    private void initEmpty(){
        for(int i = Rank.THREE.value(); i <= Rank.SIX.value(); i++){
            for(int j = File.A.value(); j <= File.H.value(); j++){
                pieces.add(PieceFactory.empty(Position.create(File.find(j), Rank.find(i))));
            }
        }
    }

    public Piece find(Position position){
        return pieces.stream()
                        .filter(p -> p.lastPosition().equals(position))
                        .findFirst()
                        .orElseGet(()->PieceFactory.empty(position));
    }

    public void remove(Piece piece) {
        pieces.remove(piece);
    }

    public void add(Piece piece) {
        pieces.add(piece);
    }

    public boolean move(Position from, Position to) {
        Piece fromPiece = find(from);
        Piece toPiece = find(to);

        if(fromPiece.isMovable(this, toPiece)){
            // 기물 제거
            if(!toPiece.isEmpty()){
                toPiece = catchPiece(toPiece);
            }
            fromPiece.swap(toPiece);

            // 폰 승진
            if(fromPiece.possiblePawnPromotion()){
                promotionPawn(fromPiece);
            }
            return true;
        }
        return false;
    }

    private Piece catchPiece(Piece piece){
        Piece empty = PieceFactory.empty(piece.lastPosition());
        remove(piece);
        add(empty);
        return empty;
    }

    private void promotionPawn(Piece piece){
        remove(piece);
        add(PieceFactory.queen(piece));
    }

    public List<Piece> display(Rank r){
        return pieces.stream()
                        .filter(p -> p.lastPosition().equalRank(r))
                        .sorted()
                        .collect(Collectors.toList());
    }

    public boolean hasKing(Color color) {
        return pieces.stream()
                        .filter(p->p.equalColor(color))
                        .anyMatch(King.class::isInstance);
    }

    public int score(Color competitorPlayerColor) {
        return TOTAL_SCORE - pieces.stream()
                                    .filter(p->p.equalColor(competitorPlayerColor))
                                    .mapToInt(Piece::getScore)
                                    .sum();
    }
}
