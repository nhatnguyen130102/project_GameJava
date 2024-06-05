package levels;

public class Level {
    private int [][] lvlData;
    public Level(int[][] lvlData){
        this.lvlData = lvlData;
    }
    // lấy tile ở toạ độ tham số và trả về giá trị của tile
    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }
    // lấy danh sách tile
    public int [][]getLevelData(){
        return lvlData;
    }
}
