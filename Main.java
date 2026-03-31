
public class Main {

    String azul = "\u001B[44m \u001B[0m";
    String rojo = "\u001B[41m \u001B[0m";
    String verde = "\u001B[42m \u001B[0m";
    String amarillo = "\u001B[43m \u001B[0m";
    boolean colocar = false;

    public static void main(String[] args) {
        ///////////////////////////////////////////////////////////
        /// Creo todo
        Main m = new Main();
        String[][] tablero = new String[20][100];

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                tablero[i][j] = m.azul;
            }
        }
        int tick = 0;
        boolean dead = true;
        int direccion = 0; // 0 = derecha, 1 = abajo, 2 = izquierda, 3 = arriba
        int comida = 0;
        ///
        ////////////////////////////////////////////////////////////
        while (tick < 1000 && dead) {

            // Leo la dirección
            int c = 0;
            try {
                c = System.in.read();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            if (c == 'd') {
                direccion = 0;
            } else if (c == 's') {
                direccion = 1;
            } else if (c == 'a') {
                direccion = 2;
            } else if (c == 'w') {
                direccion = 3;
            }

            // Limpio la consola
            System.out.print("\033[H\033[2J");
            System.out.flush();

            tick++; // Aumento el tick

            // Crea el snake
            if (tick <= 1) {
                // Coloca el snake en el centro del tablero
                for (int i = 0; i < tablero.length; i++) {
                    for (int j = 0; j < tablero[i].length; j++) {
                        tablero[tablero.length / 2][tablero[i].length / 2] = m.verde;
                    }
                }
                // Crea los bordes
                for (int i = 0; i < tablero.length; i++) {
                    for (int j = 0; j < tablero[i].length; j++) {
                        if (i == 0 || i == tablero.length - 1 || j == 0 || j == tablero[i].length - 1) {
                            tablero[i][j] = m.amarillo;
                        }
                    }
                }
            }

            // Creo la comida
            int n1 = (int) (Math.random() * 20);
            int n2 = (int) (Math.random() * 100);

            // Si la comida cae en el snake, se vuelve a generar
            while (tablero[n1][n2].equals(m.verde) || tablero[n1][n2].equals(m.amarillo)) {
                n1 = (int) (Math.random() * 20);
                n2 = (int) (Math.random() * 100);
            }

            // Coloco la comida
            if (comida == 0) {
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

                // Aplico la direccion de el snake
                switch (direccion) {
                    case 0: // Derecha
                        for (int i = 0; i < tablero.length; i++) {
                            for (int j = tablero[i].length - 1; j >= 0; j--) {
                                if (tablero[i][j].equals(m.verde)) {
                                    tablero[i][j] = m.azul;
                                    if (j + 1 < tablero[i].length) {
                                        tablero[i][j + 1] = m.verde;
                                    } else {
                                        dead = false;
                                    }
                                }
                            }
                        }
                        break;
                    case 1: // Abajo
                        for (int i = tablero.length - 1; i >= 0; i--) {
                            for (int j = 0; j < tablero[i].length; j++) {
                                if (tablero[i][j].equals(m.verde)) {
                                    tablero[i][j] = m.azul;
                                    if (i + 1 < tablero.length - 1) {
                                        tablero[i + 1][j] = m.verde;
                                    } else {
                                        dead = false;
                                    }
                                }
                            }
                        }
                        break;
                    case 2: // Izquierda
                        for (int i = 0; i < tablero.length; i++) {
                            for (int j = 0; j < tablero[i].length; j++) {
                                if (tablero[i][j].equals(m.verde)) {
                                    tablero[i][j] = m.azul;
                                    if (j - 1 >= 0) {
                                        tablero[i][j - 1] = m.verde;
                                    } else {
                                        dead = false;
                                    }
                                }
                            }
                        }
                        break;
                    case 3: // Arriba
                        for (int i = 0; i < tablero.length; i++) {
                            for (int j = 0; j < tablero[i].length; j++) {
                                if (tablero[i][j].equals(m.verde)) {
                                    tablero[i][j] = m.azul;
                                    if (i - 1 >= 1) {
                                        tablero[i - 1][j] = m.verde;
                                    } else {
                                        dead = false;
                                    }
                                }
                            }
                        }
                        break;
                }

                // Imprimo el tablero
                for (int i = 0; i < tablero.length; i++) {
                    for (int j = 0; j < tablero[i].length; j++) {
                        System.out.print(tablero[i][j]);
                    }
                    System.out.println("");
                }
                System.out.println("Segundos: " + tick);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Muerte
            int tick2 = 0;
            while (!dead) {
                switch (tick2) {
                    case 0:
                        System.out.println("Has muerto");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        System.out.println("Has muerto.");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 2:
                        System.out.println("Has muerto..");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        System.out.println("Has muerto...");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                tick2++;
                if (tick2 > 3) {
                    break; // Si pònes dead = true, no mueres realmente, solo se muestra el mensaje de muerte y sigue el juego, pero si pones break, se termina el juego realmente.
                }
            }
        }
    }
}
