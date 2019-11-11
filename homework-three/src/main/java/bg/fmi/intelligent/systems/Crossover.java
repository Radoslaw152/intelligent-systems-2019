package bg.fmi.intelligent.systems;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Crossover {
    private Path first;
    private Path second;

    public Crossover crossPaths() {
        int[] firstParent = first.getPath();
        int[] secondParent = second.getPath();

        int size = firstParent.length;
        int[] firstChild = new int[size];
        int[] secondChild = new int[size];

        int left = Path.getRandom(size);
        int right = left + 1 + Path.getRandom(size - left - 1);

        for (int i = 0; i < size; ++i) {
            if (i >= left && i < right) {
                firstChild[i] = firstParent[i];
                secondChild[i] = secondParent[i];
            } else {
                firstChild[i] = -1;
                secondChild[i] = -1;
            }

        }

        for (int i = left; i < right; ++i) {
            boolean checkIfLeftCopied = false;
            boolean checkIfRightCopied = false;
            for (int j = left; j < right; ++j) {
                if (secondChild[i] == firstChild[j]) {
                    checkIfLeftCopied = true;
                }
                if (firstChild[i] == secondChild[j]) {
                    checkIfRightCopied = true;
                }
                if (checkIfLeftCopied && checkIfRightCopied) {
                    break;
                }
            }

            if (!checkIfLeftCopied) {
                for (int j = 0; j < size; ++j) {
                    if (firstChild[i] == secondParent[j]) {
                        if (j < left || j >= right) {
                            firstChild[j] = secondChild[i];
                        } else {
                            for (int k = 0; k < size; ++k) {
                                if (firstChild[j] == secondParent[k]) {
                                    firstChild[k] = secondChild[i];
                                }
                            }
                        }

                    }
                }
            }

            if (!checkIfRightCopied) {
                for (int j = 0; j < size; ++j) {
                    if (secondChild[i] == firstParent[j]) {
                        if (j < left || j >= right) {
                            secondChild[j] = firstChild[i];
                        } else {
                            for (int k = 0; k < size; ++k) {
                                if (secondChild[j] == firstParent[k]) {
                                    secondChild[k] = firstChild[i];
                                }
                            }
                        }

                    }
                }
            }
        }

        for(int i = 0; i < size;++i) {
            if(firstChild[i] == -1) {
                firstChild[i] = secondParent[i];
            }
            if (secondChild[i] == -1) {
                secondChild[i] = firstParent[i];
            }
        }

        Path firstPath = new Path(first.getMatrix(), firstChild);
        Path secondPath = new Path(second.getMatrix(), secondChild);

        return new Crossover(firstPath, secondPath);
    }
}
