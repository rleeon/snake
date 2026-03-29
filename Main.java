public class Main {

    String azul = "\u001B[44m \u001B[0m";
    String rojo = "\u001B[41m \u001B[0m";
    String verde = "\u001B[42m \u001B[0m";
    String amarillo = "\u001B[43m \u001B[0m";
    boolean colocar = false;

    public static void main(String[] args) {
        Mavenproject1 m = new Mavenproject1();
        String[][] tablero = new String[10][100];

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                tablero[i][j] = m.azul;
            }
        }
        int tick = 0;
        boolean dead = true;
        while (tick < 1000 && dead) {
            tick++;
            if (tick <= 1) {
                for (int i = 0; i < tablero.length; i++) {
                    for (int j = 0; j < tablero[i].length; j++) {
                        tablero[tablero.length / 2][tablero[i].length / 2] = m.verde;
                    }
                }
            }
            if (tick <= 999) {
                for (int i = 0; i < tablero.length; i++) {
                    for (int j = 0; j < tablero[i].length; j++) {
                        tablero[(tablero.length / 2) + 1][(tablero[i].length / 2) + 1] = m.verde;
                    }
                }
            }

            int n1 = (int) (Math.random() * 10);
            int n2 = (int) (Math.random() * 100);

            while (tablero[n1][n2].equals(m.verde)) {
                n1 = (int) (Math.random() * 10);
                n2 = (int) (Math.random() * 100);
            }

            if (tablero[n1][n2].equals(m.azul)) {
                for (int i = 0; i < tablero.length; i++) {
                    for (int j = 0; j < tablero[i].length; j++) {
                        if (tablero[n1][n2].equals(m.azul)) {
                            tablero[n1][n2] = m.rojo;
                        } else if (!tablero[n1][n2].equals(m.verde) && !tablero[n1][n2].equals(m.rojo)) {
                            tablero[i][j] = m.azul;
                        }
                    }
                }
            }

            for (int i = 0; i < tablero.length; i++) {
                for (int j = 0; j < tablero[i].length; j++) {
                    System.out.print(tablero[i][j]);
                }
                System.out.println("");
            }
                System.out.println("Tick: " + tick);
        }
    }
}
