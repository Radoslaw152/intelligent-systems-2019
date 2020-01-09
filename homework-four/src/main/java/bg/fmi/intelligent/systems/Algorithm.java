package bg.fmi.intelligent.systems;

import java.util.Scanner;
public class Algorithm {

    public static void main(String[] args) {
        char[][] board = generateNewMatrix();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, type true (PC first) or false (you first):");
        boolean pcFirst = scanner.nextBoolean();
        Node node = new Node(board,true);

        while(!node.checkIfSolved()) {
            if(!pcFirst) {
                node.setValueInBoard(scanner);
            }
            node = node.traverse();
            printMatrix(node.getBoard());
            pcFirst = false;
        }
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
