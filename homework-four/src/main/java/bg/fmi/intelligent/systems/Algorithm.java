package bg.fmi.intelligent.systems;

import java.util.Scanner;
public class Algorithm {

    public static void main(String[] args) {
        char[][] board = generateNewMatrix();
        Scanner scanner = new Scanner(System.in);
        boolean isMax = scanner.nextBoolean();
        Node node = new Node(board,isMax);
        if(isMax) {
            node = node.traverse();
        }
        while(!node.foundValue()) {
            printMatrix(node.getBoard());
            node.setValueInBoard(scanner);
            node = node.traverse();
        }
        printMatrix(node.getBoard());
    }

    public static  void printMatrix(char[][] board) {
        for (char[] row : board) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    public static char[][] generateNewMatrix() {
        char[][] board = new char[3][3];
        for(char[] row : board) {
            for(int i = 0; i < row.length;++i) {
                row[i] = '_';
            }
        }
        return board;
    }
}
