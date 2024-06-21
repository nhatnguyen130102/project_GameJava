package Test;

public class Item {
    private double centerX;
    private double centerY;
    private double radius;
    private double angle;
    private double angularSpeed; // Tốc độ góc (radian/giây)
    private double x;
    private double y;

    public Item(double centerX, double centerY, double radius, double angularSpeed) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.angle = 0;
        this.angularSpeed = angularSpeed;
        updatePosition();
    }

    public void update(double deltaTime) {
        angle += angularSpeed * deltaTime;
        // Giới hạn góc từ 0 đến pi để chỉ di chuyển trên nửa đường tròn phía trên
        if (angle > Math.PI) {
            angle = Math.PI;
        }
        updatePosition();
    }

    private void updatePosition() {
        x = centerX + radius * Math.cos(angle);
        y = centerY - radius * Math.sin(angle); // Y di chuyển ngược vì tọa độ y tăng xuống dưới
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
