import java.util.ArrayList;

public class Main {
    private final double[][] matrix;
    private final double[] answers;
    private final boolean[] lines;

    Main(int[][] matrix, int[] answers) {
        this.matrix = parseMatrixIntToDouble(matrix);
        this.answers = parseIntToDouble(answers);
        this.lines = new boolean[answers.length];
    }

    double[] execute() {
        double element;
        int index;
        int row = 0;

        while (!isEnd(this.lines)) {

            index = getIndexMin(this.matrix, this.lines, row);
            element = matrix[index][row];

            divToElement(this.matrix[index], element);
            this.answers[index] /= element;


            ArrayList<Parallel> threads = new ArrayList<>();
            for (int i = 0; i < matrix.length; i++) {
                Parallel thread = new Parallel(this.matrix, this.answers, index, row, i);
                threads.add(thread);
            }

            threads.forEach(thread -> thread.start());

            threads.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException exception) {
                    System.out.println(exception);
                }

            });

            this.lines[index] = true;
            row++;
        }

        return getAnswer(this.matrix, this.answers);
    }

    class Parallel extends Thread {
        private double[][] matrix;
        private double[] answer;
        private int row;
        private int index;
        private int line;


        Parallel(double[][] matrix, double[] answers, int index, int row, int line) {
            this.matrix = matrix;
            this.answer = answers;
            this.index = index;
            this.row = row;
            this.line = line;
        }

        @Override
        public void run() {
            toNullifyElement(this.matrix, this.answer, this.index, this.row, line);
        }


        private void toNullifyElement(double[][] matrix, double[] answers, int exceptLine, int row, int line) {
            if (line != exceptLine) {
                double first = matrix[line][row];
                for (int j = 0; j < matrix[line].length; j++) {
                    if (matrix[line][j] != 0.) {
                        matrix[line][j] -= first * matrix[exceptLine][j];
                    }
                }
                if (answers[line] != 0.) {
                    answers[line] -= first * answers[exceptLine];
                }
            }
        }

    }

    private double[][] parseMatrixIntToDouble(int[][] matrix) {
        double[][] result = new double[matrix.length][];
        int count = 0;
        for (int[] line : matrix) {
            result[count++] = parseIntToDouble(line);
        }
        return result;
    }

    private double[] parseIntToDouble(int[] line) {
        double[] result = new double[line.length];
        for (int i = 0; i < line.length; i++) {
            result[i] = line[i];
        }
        return result;
    }


    private double[] getAnswer(double[][] matrix, double[] answers) {
        double[] result = new double[answers.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    result[j] = answers[i];
                }
            }
        }
        return result;
    }

    private boolean isEnd(boolean[] lines) {
        boolean result = true;
        for (boolean index : lines) {
            if (!index) {
                result = false;
                break;
            }
        }
        return result;
    }


    private void divToElement(double[] line, double div) {
        for (int index = 0; index < line.length; index++) {
            if (line[index] != 0.) {
                line[index] /= div;
            }
        }
    }

    private int getIndexMin(double[][] matrix, boolean[] lines, int index) {
        int res = -1;
        double min = Integer.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            if (!lines[i] && (Math.abs(matrix[i][index]) < Math.abs(min)) && matrix[i][index] != 0) {
                res = i;
                min = matrix[i][index];
            }
        }
        return res;
    }

    public static void main(String[] args) {
        final int[] ANSWERS = {20, 11, 40, 37};
        final int[][] MATRIX = {
                {1, 5, 4, 1},
                {1, 3, 2, 1},
                {2, 10, 9, 7},
                {3, 8, 9, 2}
        };

        double[] answers = new Main(MATRIX, ANSWERS).execute();

        System.out.println("Answer:");
        for (int x = 0; x < answers.length; x++) {
            System.out.println("X" + (x + 1) + "= " + answers[x]);
        }
    }

}