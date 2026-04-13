
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    String azul = "\u001B[44m \u001B[0m";
    String rojo = "\u001B[41m \u001B[0m";
    String verde = "\u001B[42m \u001B[0m";
    String amarillo = "\u001B[43m \u001B[0m";
    boolean colocar = false;

    public static void main(String[] args) {
        ///////////////////////////////////////////////////////////
        /// Creo todo
        int i1 = 20; // Filas
        int j1 = 40; // Columnas
        Main m = new Main();
        String[][] tablero = new String[i1][j1];

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                tablero[i][j] = m.azul;
            }
        }
        int tick = 0;
        boolean dead = true;
        boolean debug = false;
        int comida = 0;
        int comidacomida = 0;

        int x = 0;
        int y = 0;
        String[][] cabeza = new String[i1][j1];
        Deque<String[]> cuerpo = new ArrayDeque<>();
        ///
        ////////////////////////////////////////////////////////////
        ///
        /// Siempre se ejecuta antes de poner en modo raw
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", "stty sane < /dev/tty" }).waitFor();
            } catch (Exception ignored) {
            }
        }));
        ///
        /// Coloco el terminal en modo raw para que no haga falta pulsar enter para leer
        /// la entrada, esto es para linux, en windows no es posilbe
        try {
            Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", "stty raw -echo < /dev/tty" }).waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AtomicInteger direccion = new AtomicInteger(0);

        // El hilo de input:
        Thread inputThread = new Thread(() -> {
            while (true) {
                try {
                    int c = System.in.read();
                    while (System.in.available() > 0) {
                        System.in.read();
                    }
                    switch (c) {
                        case 'd':
                            direccion.set(0);
                            break;
                        case 's':
                            direccion.set(1);
                            break;
                        case 'a':
                            direccion.set(2);
                            break;
                        case 'w':
                            direccion.set(3);
                            break;
                        case 'q':
                            direccion.set(4);
                            break;
                    }
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();

        while (tick < 10000000 && dead) {

            // Limpio la consola
            System.out.print("\033[H\033[2J");
            System.out.flush();

            tick++; // Aumento el tick

            // Crea el snake
            if (tick <= 1) {
                cuerpo.addFirst(new String[] { "D" });
                // Coloca el snake en el centro del tablero
                x = tablero.length / 2;
                y = tablero[0].length / 2;
                tablero[x][y] = m.verde;
                cabeza[x][y] = m.verde;
            }
            // borra todo y lo pone azul.
            for (int i = 0; i < tablero.length; i++) {
                for (int j = 0; j < tablero[i].length; j++) {
                    if (i == 0 || i == tablero.length + 1 || j == 0 || j == tablero[i].length + 1 || i == 0 || i == (tablero.length - 1) || j == 0 || j == (tablero[i].length - 1)) {
                        tablero[i][j] = m.azul;
                    }
                }
            }
                // Crea los bordes.
                for (int i = 0; i < tablero.length; i++) {
                    for (int j = 0; j < tablero[i].length; j++) {
                        if (i == 0 || i == tablero.length - 1 || j == 0 || j == tablero[i].length - 1) {
                            tablero[i][j] = m.amarillo;
                        }
                    }
                }
            int x2 = x;
            int y2 = y;

            for (String[] arr : cuerpo) {
                switch (arr[0]) {
                    case "D":
                        if (!tablero[x2][y2 - 1].equals(m.rojo) && !tablero[x2][y2 - 1].equals(m.amarillo)) {
                            tablero[x2][y2 - 1] = m.azul;
                        }
                        y2 -= 1;
                        break;
                    case "S":
                        if (!tablero[x2 - 1][y2].equals(m.rojo) && !tablero[x2 - 1][y2].equals(m.amarillo)) {
                            tablero[x2 - 1][y2] = m.azul;
                        }
                        x2 -= 1;
                        break;
                    case "A":
                        if (!tablero[x2][y2 + 1].equals(m.rojo) && !tablero[x2][y2 + 1].equals(m.amarillo)) {
                            tablero[x2][y2 + 1] = m.azul;
                        }
                        y2 += 1;
                        break;
                    case "W":
                        if (!tablero[x2 + 1][y2].equals(m.rojo) && !tablero[x2 + 1][y2].equals(m.amarillo)) {
                            tablero[x2 + 1][y2] = m.azul;
                        }
                        x2 += 1;
                        break;

                    default:
                        break;
                }
            }

            // Creo la comida
            int n1 = (int) (Math.random() * i1);
            int n2 = (int) (Math.random() * j1);

            // Si la comida cae en el snake, se vuelve a generar
            while (tablero[n1][n2].equals(m.verde) || tablero[n1][n2].equals(m.amarillo)) {
                n1 = (int) (Math.random() * i1);
                n2 = (int) (Math.random() * j1);
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
                    comida++;
                }
            }

            // Aplico la direccion de el snake
            switch (direccion.get()) {
                case 0: // Derecha
                    for (int i = 0; i < tablero.length; i++) {
                        for (int j = tablero[i].length - 1; j >= 0; j--) {
                            if (i == x && j == y) {
                                if (tick != 1) {
                                    if (tablero[i][j + 1].equals(m.verde)) {
                                        // dead = false;
                                    }
                                    if (tablero[i][j + 1].equals(m.amarillo)) {
                                        // dead = false;
                                    }
                                }
                                if (!tablero[i][j + 1].equals(m.rojo)) {
                                    cuerpo.removeLast();
                                    cuerpo.addFirst(new String[] { "D" });
                                    y += 1;
                                }
                                if (tablero[i][j + 1].equals(m.rojo)) {
                                    comidacomida++;
                                    comida = 0;
                                    cuerpo.removeLast();
                                    cuerpo.addFirst(new String[] { "D" });
                                    cuerpo.addFirst(new String[] { "D" });

                                    y += 1;
                                }
                            }
                        }
                    }
                    break;
                case 1: // Abajo
                    for (int i = tablero.length - 1; i >= 0; i--) {
                        for (int j = 0; j < tablero[i].length; j++) {
                            if (i == x && j == y) {
                                if (tick != 1) {
                                    if (tablero[i + 1][j].equals(m.verde)) {
                                        // dead = false;
                                    }
                                    if (tablero[i + 1][j].equals(m.amarillo)) {
                                        // dead = false;
                                    }
                                }
                                if (!tablero[i + 1][j].equals(m.rojo)) {
                                    cuerpo.removeLast();
                                    cuerpo.addFirst(new String[] { "S" });
                                    x += 1;
                                }
                                if (tablero[i + 1][j].equals(m.rojo)) {
                                    comidacomida++;
                                    comida = 0;
                                    cuerpo.removeLast();
                                    cuerpo.addFirst(new String[] { "S" });
                                    cuerpo.addFirst(new String[] { "S" });

                                    x += 1;
                                }
                            }
                        }
                    }
                    break;
                case 2: // Izquierda
                    for (int i = 0; i < tablero.length; i++) {
                        for (int j = 0; j < tablero[i].length; j++) {
                            if (i == x && j == y) {
                                if (tick != 1) {
                                    if (tablero[i][j - 1].equals(m.verde)) {
                                        // dead = false;
                                    }
                                    if (tablero[i][j - 1].equals(m.amarillo)) {
                                        // dead = false;
                                    }
                                }
                                if (!tablero[i][j - 1].equals(m.rojo)) {
                                    cuerpo.removeLast();
                                    cuerpo.addFirst(new String[] { "A" });
                                    y -= 1;
                                }
                                if (tablero[i][j - 1].equals(m.rojo)) {
                                    comidacomida++;
                                    comida = 0;
                                    cuerpo.removeLast();
                                    cuerpo.addFirst(new String[] { "A" });
                                    cuerpo.addFirst(new String[] { "A" });

                                    y -= 1;
                                }
                            }
                        }
                    }
                    break;
                case 3: // Arriba
                    for (int i = 0; i < tablero.length; i++) {
                        for (int j = 0; j < tablero[i].length; j++) {
                            if (i == x && j == y) {
                                if (tick != 1) {
                                    if (tablero[i - 1][j].equals(m.verde)) {
                                        // dead = false;
                                    }
                                    if (tablero[i - 1][j].equals(m.amarillo)) {
                                        // dead = false;
                                    }
                                }
                                if (!tablero[i - 1][j].equals(m.rojo)) {
                                    cuerpo.removeLast();
                                    cuerpo.addFirst(new String[] { "W" });
                                    x -= 1;
                                }
                                if (tablero[i - 1][j].equals(m.rojo)) {
                                    comidacomida++;
                                    comida = 0;
                                    cuerpo.removeLast();
                                    cuerpo.addFirst(new String[] { "W" });
                                    cuerpo.addFirst(new String[] { "W" });

                                    x -= 1;
                                }
                            }
                        }
                    }
                    break;
                case 4:
                    cuerpo.addFirst(new String[] { "" });
                    break;
            }
            x2 = x;
            y2 = y;
            for (String[] arr : cuerpo) {
                switch (arr[0]) {
                    case "D":
                        tablero[x2][y2 + 1] = m.verde;
                        y2 += 1;
                        break;
                    case "S":
                        tablero[x2 + 1][y2] = m.verde;
                        x2 += 1;
                        break;
                    case "A":
                        tablero[x2][y2 - 1] = m.verde;
                        y2 -= 1;
                        break;
                    case "W":
                        tablero[x2 - 1][y2] = m.verde;
                        x2 -= 1;
                        break;

                    default:
                        break;
                }
            }

            // Imprimo el tablero
            for (int i = 0; i < tablero.length; i++) {
                for (int j = 0; j < tablero[i].length; j++) {
                    System.out.print(tablero[i][j]);
                }
                System.out.print("\r\n");
            }
            System.out.print("Segundos: " + tick + "\r\n");
            System.out.print("Comida: " + comidacomida + "\r\n");
            for (String[] arr : cuerpo) {
                System.out.println(Arrays.toString(arr) + "\r\n");
            }

            try {
                Thread.sleep(159);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Muerte
            int tick2 = 0;
            while (!dead) {
                switch (tick2) {
                    case 0:
                        System.out.println("Has muerto \r\n");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        System.out.println("Has muerto. \r\n");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 2:
                        System.out.println("Has muerto.. \r\n");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        System.out.println("Has muerto... \r\n");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                tick2++;
                if (tick2 == 3) {
                    break;
                } // si pones (dead = true) creara un bucle y no saldra nunca de el juego, al
                  // poner break si, esta dentro de el if para que se ejecute lo anterior.
                  // Osea cambia este if si modificas la muerte (dead)
            }
        }
    }
}
