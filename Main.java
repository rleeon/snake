
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
        int comida = 0;
        int comidacomida = 0;

        int x = 0;
        int y = 0;
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
                    }
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();

        while (tick < 1000 && dead) {

            // Limpio la consola
            System.out.print("\033[H\033[2J");
            System.out.flush();

            tick++; // Aumento el tick

            // Crea el snake
            if (tick <= 1) {
                cuerpo.addFirst(new String[] { "D" });
                // Coloca el snake en el centro del tablero
                for (int i = 0; i < tablero.length; i++) {
                    for (int j = 0; j < tablero[i].length; j++) {
                        tablero[tablero.length / 2][tablero[i].length / 2] = m.verde;
                    }
                }
                // Crea los bordes, se hace solo al principio para que no se cree los bordes
                // infinitamente
                for (int i = 0; i < tablero.length; i++) {
                    for (int j = 0; j < tablero[i].length; j++) {
                        if (i == 0 || i == tablero.length - 1 || j == 0 || j == tablero[i].length - 1) {
                            tablero[i][j] = m.amarillo;
                        }
                    }
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
                            if (tablero[i][j].equals(m.verde)) {
                                tablero[i][j] = m.azul;
                                if (!tablero[i][j + 1].equals(m.rojo)){
                                    cuerpo.removeLast();
                                    cuerpo.addFirst(new String[] { "D" });
                                }
                                if (tablero[i][j + 1].equals(m.rojo)) { // Si la comida está a la derecha del snake se
                                                                        // come la comida
                                    tablero[i][j + 1] = m.verde;
                                    comidacomida++;

                                    comida = 0;
                                    cuerpo.addFirst(new String[] { "D" });

                                } else if (!tablero[i][j + 1].equals(m.amarillo)) {
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
                                if (!tablero[i + 1][j].equals(m.rojo)){
                                    cuerpo.removeLast();
                                    cuerpo.addFirst(new String[] { "S" });
                                }
                                if (tablero[i + 1][j].equals(m.rojo)) { // Si la comida está abajo del snake se come la
                                                                        // comida
                                    tablero[i + 1][j] = m.verde;
                                    comidacomida++;

                                    comida = 0;
                                    cuerpo.addFirst(new String[] { "S" });

                                } else if (!tablero[i + 1][j].equals(m.amarillo)) {
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
                                if (!tablero[i][j - 1].equals(m.rojo)){
                                    cuerpo.removeLast();
                                    cuerpo.addFirst(new String[] { "A" });
                                }
                                if (tablero[i][j - 1].equals(m.rojo)) { // Si la comida está a la izquierda del snake se
                                                                        // come la comida
                                    tablero[i][j - 1] = m.verde;
                                    comidacomida++;

                                    comida = 0;
                                    cuerpo.addFirst(new String[] { "A" });

                                } else if (!tablero[i][j - 1].equals(m.amarillo)) {
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
                                if (!tablero[i - 1][j].equals(m.rojo)){
                                    cuerpo.removeLast();
                                    cuerpo.addFirst(new String[] { "W" });
                                }
                                if (tablero[i - 1][j].equals(m.rojo)) { // Si la comida está arriba del snake se come la
                                                                        // comida
                                    tablero[i - 1][j] = m.verde;
                                    comidacomida++;

                                    comida = 0;
                                    cuerpo.addFirst(new String[] { "W" });

                                } else if (!tablero[i - 1][j].equals(m.amarillo)) {
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
                System.out.print("\r\n");
            }
            System.out.print("Segundos: " + tick + "\r\n");
            System.out.print("Comida: " + comidacomida + "\r\n");
            for (String[] arr : cuerpo) {
                System.out.println(Arrays.toString(arr) + "\r\n");
            }

            try {
                Thread.sleep(259);
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
