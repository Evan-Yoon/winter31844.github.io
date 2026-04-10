package javaStudy;

public class Car {

        private int speed;
        private boolean stop;
        public Tire tire;

        public void run(){
            this.tire.roll();
        }

        public void speedUp() {
            speed += 1;
        }

        public final void stop() { // *이렇게 final을 붙이면 오버라이딩 불가
            System.out.println("차를 멈춥니다.");
            speed = 0;
        }

        public int getSpeed() {
            return this.speed;
        }

        public void setSpeed(int speed) {
            if(speed<0) {
                this.speed = 0;
            } else {
            this.speed = speed;
            }
        }

        public boolean isStop() {
            return this.stop;
        }

        public void setStop(boolean stop) {
            this.stop = stop;
            if(stop == true)
                this.speed = 0;
        }
}
