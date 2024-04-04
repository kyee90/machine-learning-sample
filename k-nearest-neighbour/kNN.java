import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/*
 * ->   k-NearestNeighbour Sample Code by Kieran Yee
 * ->   Uses Wine dataset: https://archive.ics.uci.edu/dataset/109/wine
 */
public class kNN {
    ArrayList<Wine> trainingset = new ArrayList<Wine>();
    ArrayList<Wine> testset = new ArrayList<Wine>();
    int k = 1;

    public kNN(ArrayList<Wine> training, ArrayList<Wine> test, int kvalue) {
        this.trainingset = training;
        this.testset = test;
        this.k = kvalue;
    }

    /*
     * -> quick method for testing
     */
    public void knntest() {
        if (!trainingset.isEmpty() && !testset.isEmpty()) {
            Wine testwine = testset.get(1);
            Wine trainwine = trainingset.get(1);
            testwine.normdistance(trainwine);
        }
    }

    /*
     * -> classification without normalisation
     */
    public void classify() {
        double prediction = 0.0;
        for (Wine w1 : testset) {
            double lowdist = Integer.MAX_VALUE;
            Wine spare = null;
            int spareclass = 0;
            for (Wine w2 : trainingset) {
                if (distance(w1, w2) < lowdist) {
                    spare = w2;
                    lowdist = distance(w1, w2);
                    spareclass = w2.idclass();
                }
            }
            if (spare.idclass() == w1.idclass()) {
                prediction++;
            }
            System.out.println("Dist: " + lowdist);
            System.out.println("Predicted class: " + spareclass);
            System.out.println("Correct class: " + w1.idclass() + "\n");
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (Exception e) {
            }
        }
        double correctfinds = prediction / trainingset.size();
        System.out.println("Correct class predictions = " + correctfinds * 100);
    }

    /*
     * -> classification WITH normalisation
     */
    public void classifynorm() throws FileNotFoundException {
        // PrintWriter writer = new
        // PrintWriter(System.getProperty("user.dir")+"/part1/"+"part1sampleoutput.txt");
        // writer.println("doing kNN classification with knn "+this.k);
        // the set of nearest neighbours
        ArrayList<Wine> closewines = new ArrayList<Wine>(this.k);

        // var for calculating prediction accuracy
        double prediction = 0.0;

        // this is the loop through the training set to find nearest neighbours
        for (Wine w1 : testset) {

            for (Wine w2 : trainingset) {

                if (closewines.size() == this.k) {
                    for (int i = 0; i < closewines.size(); i++) {
                        if (normdistance(w1, w2) < normdistance(w1, closewines.get(i))) {
                            closewines.remove(closewines.get(i));
                            if (!closewines.contains(w2)) {
                                closewines.add(w2);
                            }
                        }
                    }
                }

                if (!closewines.contains(w2) && closewines.size() < this.k) {
                    {
                        closewines.add(w2);
                    }
                }
            }

            // if kNN is 1
            if (this.k == 1) {
                Wine win = closewines.get(0);
                System.out.println("Predicted class " + win.idclass());
                // writer.println("Predicted class " + win.idclass());
                if (w1.idclass() == win.idclass()) {
                    System.out.println("Correct!");
                    // writer.println("Correct!");
                    prediction++;
                } else {
                    System.out.println("Wrong prediction! Correct class: " + w1.idclass());
                    // writer.println("Wrong prediction! Correct class: " + w1.idclass());
                }
            }

            // if kNN > 1
            if (this.k > 1) {
                int count1 = 0;
                int count2 = 0;
                int count3 = 0;
                for (Wine w4 : closewines) {
                    if (w4.idclass() == 1) {
                        count1++;
                    }
                    if (w4.idclass() == 2) {
                        count2++;
                    }
                    if (w4.idclass() == 3) {
                        count3++;
                    }
                }
                if (count1 > count2 && count1 > count3) {
                    System.out.println("Predicted class: 1");
                    // writer.println("Predicted class: 1");
                    if (w1.idclass() == 1) {
                        System.out.println("Correct!");
                        // writer.println("Correct!");
                        prediction++;
                    } else {
                        System.out.println("Wrong prediction! Correct class: " + w1.idclass());
                        // writer.println("Wrong prediction! Correct class: " + w1.idclass());
                    }
                }
                if (count2 > count1 && count2 > count3) {
                    System.out.println("Predicted class: 2");
                    // writer.println("Predicted class: 2");
                    if (w1.idclass() == 2) {
                        System.out.println("Correct!");
                        // writer.println("Correct!");
                        prediction++;
                    } else {
                        System.out.println("Wrong prediction! Correct class: " + w1.idclass());
                        // writer.println("Wrong prediction! Correct class: " + w1.idclass());
                    }
                }
                if (count3 > count1 && count3 > count2) {
                    System.out.println("Predicted class: 3");
                    // writer.println("Predicted class: 3");
                    if (w1.idclass() == 3) {
                        System.out.println("Correct!");
                        // writer.println("Correct!");
                        prediction++;
                    } else {
                        System.out.println("Wrong prediction! Correct class: " + w1.idclass());
                        // writer.println("Wrong prediction! Correct class: " + w1.idclass());
                    }
                    // if class count is equal
                } else if (count3 == count2 && count2 == count1) {
                    int randomNum = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                    System.out.println("Predicted class: " + randomNum);
                    // writer.println("Predicted class: " + randomNum);
                    if (w1.idclass() == randomNum) {
                        System.out.println("Correct!");
                        // writer.println("Correct!");
                        prediction++;
                    } else {
                        System.out.println("Wrong prediction! Correct class: " + w1.idclass());
                        // writer.println("Wrong prediction! Correct class: " + w1.idclass());
                    }
                }
            }

            // small delay for output!
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (Exception e) {
            }
        }

        double correctfinds = prediction
                / trainingset.size();
        System.out.println("\nPredictions correct : " + prediction);
        System.out.println("Total tests: " + trainingset.size());
        System.out.println("Classification accuracy = " + correctfinds * 100);
        // writer.println("\nPredictions correct : " + prediction);
        // writer.println("Total tests: " + trainingset.size());
        // writer.println("Classification accuracy = " + correctfinds * 100);
        // writer.close();
    }

    /*
     * -> distance measure without normalisation
     */
    public double distance(Wine wine1, Wine wine2) {
        double dist;
        double normalc = Math.pow(wine2.alc() - wine1.alc(), 2);
        double normal_acid = Math.pow(wine2.mal_acid() - wine1.mal_acid(), 2);
        double normash = Math.pow(wine2.ash() - wine1.ash(), 2);
        double normalc_ash = Math.pow(wine2.alc_ash() - wine1.alc_ash(), 2);
        double normmag = Math.pow(wine2.mag() - wine1.mag(), 2);
        double normphenols = Math.pow(wine2.phenols() - wine1.phenols(), 2);
        double normflava = Math.pow(wine2.flava() - wine1.flava(), 2);
        double normnonflava = Math.pow(wine2.nonflava() - wine1.nonflava(), 2);
        double normcyanins = Math.pow(wine2.cyanins() - wine1.cyanins(), 2);
        double normcolorint = Math.pow(wine2.colorint() - wine1.colorint(), 2);
        double normhue = Math.pow(wine2.hue() - wine1.hue(), 2);
        double normod280 = Math.pow(wine2.od280() - wine1.od280(), 2);
        double normprol = Math.pow(wine2.proline() - wine1.proline(), 2);
        dist = normalc + normal_acid + normash + normalc_ash + normmag + normphenols + normflava + normnonflava
                + normcyanins + normcolorint + normhue + normod280 + normprol;
        dist = Math.sqrt(dist);
        return dist;
    }

    /*
     * -> distance measure with normalised distance calculation using max / min
     * values from the training set
     */
    public double normdistance(Wine wine1, Wine wine2) {
        double dist;

        // max/min alcohol
        Wine maxalcWine = trainingset.stream().max(Comparator.comparing(Wine::alc))
                .orElseThrow(NoSuchElementException::new);
        Wine minalcWine = trainingset.stream().min(Comparator.comparing(Wine::alc))
                .orElseThrow(NoSuchElementException::new);
        double maxalc = maxalcWine.alc();
        double minalc = minalcWine.alc();
        double normalc = Math.pow(wine2.alc() - wine1.alc(), 2) / Math.pow(maxalc - minalc, 2);

        // max/min malic_acid
        Wine maxmalWine = trainingset.stream().max(Comparator.comparing(Wine::mal_acid))
                .orElseThrow(NoSuchElementException::new);
        Wine minmalcWine = trainingset.stream().min(Comparator.comparing(Wine::mal_acid))
                .orElseThrow(NoSuchElementException::new);
        double maxmal_acid = maxmalWine.mal_acid();
        double minmal_acid = minmalcWine.mal_acid();
        double normal_acid = Math.pow(wine2.mal_acid() - wine1.mal_acid(), 2) / Math.pow(maxmal_acid - minmal_acid, 2);

        // max/min ash
        Wine maxashWine = trainingset.stream().max(Comparator.comparing(Wine::ash))
                .orElseThrow(NoSuchElementException::new);
        Wine minashWine = trainingset.stream().min(Comparator.comparing(Wine::ash))
                .orElseThrow(NoSuchElementException::new);
        double maxash = maxashWine.ash();
        double minash = minashWine.ash();
        double normash = Math.pow(wine2.ash() - wine1.ash(), 2) / Math.pow(maxash - minash, 2);

        // max/min alcalinity of ash
        Wine maxalcashWine = trainingset.stream().max(Comparator.comparing(Wine::alc_ash))
                .orElseThrow(NoSuchElementException::new);
        Wine minalcashWine = trainingset.stream().min(Comparator.comparing(Wine::alc_ash))
                .orElseThrow(NoSuchElementException::new);
        double maxalcash = maxalcashWine.alc_ash();
        double minalcash = minalcashWine.alc_ash();
        double normalc_ash = Math.pow(wine2.alc_ash() - wine1.alc_ash(), 2) / Math.pow(maxalcash - minalcash, 2);

        // max/min magnesium
        Wine maxmagWine = trainingset.stream().max(Comparator.comparing(Wine::mag))
                .orElseThrow(NoSuchElementException::new);
        Wine minmagWine = trainingset.stream().min(Comparator.comparing(Wine::mag))
                .orElseThrow(NoSuchElementException::new);
        double maxmag = maxmagWine.mag();
        double minmag = minmagWine.mag();
        double normmag = Math.pow(wine2.mag() - wine1.mag(), 2) / Math.pow(maxmag - minmag, 2);

        // max/min phenols
        Wine maxphenolWine = trainingset.stream().max(Comparator.comparing(Wine::phenols))
                .orElseThrow(NoSuchElementException::new);
        Wine minphenolWine = trainingset.stream().min(Comparator.comparing(Wine::phenols))
                .orElseThrow(NoSuchElementException::new);
        double maxphenol = maxphenolWine.phenols();
        double minphenol = minphenolWine.phenols();
        double normphenols = Math.pow(wine2.phenols() - wine1.phenols(), 2) / Math.pow(maxphenol - minphenol, 2);

        // max/min flava
        Wine maxflavaWine = trainingset.stream().max(Comparator.comparing(Wine::flava))
                .orElseThrow(NoSuchElementException::new);
        Wine minflavaWine = trainingset.stream().min(Comparator.comparing(Wine::flava))
                .orElseThrow(NoSuchElementException::new);
        double maxflava = maxflavaWine.flava();
        double minflava = minflavaWine.flava();
        double normflava = Math.pow(wine2.flava() - wine1.flava(), 2) / Math.pow(maxflava - minflava, 2);

        // max/min nonflava
        Wine maxnonflavaWine = trainingset.stream().max(Comparator.comparing(Wine::nonflava))
                .orElseThrow(NoSuchElementException::new);
        Wine minnonflavaWine = trainingset.stream().min(Comparator.comparing(Wine::nonflava))
                .orElseThrow(NoSuchElementException::new);
        double maxnonflava = maxnonflavaWine.nonflava();
        double minnonflava = minnonflavaWine.nonflava();
        double normnonflava = Math.pow(wine2.nonflava() - wine1.nonflava(), 2) / Math.pow(maxnonflava - minnonflava, 2);

        // max/min cyanins
        Wine maxcyanWine = trainingset.stream().max(Comparator.comparing(Wine::cyanins))
                .orElseThrow(NoSuchElementException::new);
        Wine mincyanWine = trainingset.stream().min(Comparator.comparing(Wine::cyanins))
                .orElseThrow(NoSuchElementException::new);
        double maxcyan = maxcyanWine.cyanins();
        double mincyan = mincyanWine.cyanins();
        double normcyanins = Math.pow(wine2.cyanins() - wine1.cyanins(), 2) / Math.pow(maxcyan - mincyan, 2);

        // max/min color intesntiy
        Wine maxcolWine = trainingset.stream().max(Comparator.comparing(Wine::colorint))
                .orElseThrow(NoSuchElementException::new);
        Wine mincolWine = trainingset.stream().min(Comparator.comparing(Wine::colorint))
                .orElseThrow(NoSuchElementException::new);
        double maxcol = maxcolWine.colorint();
        double mincol = mincolWine.colorint();
        double normcolorint = Math.pow(wine2.colorint() - wine1.colorint(), 2) / Math.pow(maxcol - mincol, 2);

        // max/min hue
        Wine maxhueWine = trainingset.stream().max(Comparator.comparing(Wine::hue))
                .orElseThrow(NoSuchElementException::new);
        Wine minhueWine = trainingset.stream().min(Comparator.comparing(Wine::hue))
                .orElseThrow(NoSuchElementException::new);
        double maxhue = maxhueWine.hue();
        double minhue = minhueWine.hue();
        double normhue = Math.pow(wine2.hue() - wine1.hue(), 2) / Math.pow(maxhue - minhue, 2);

        // max/min od280
        Wine maxod280Wine = trainingset.stream().max(Comparator.comparing(Wine::od280))
                .orElseThrow(NoSuchElementException::new);
        Wine minod280Wine = trainingset.stream().min(Comparator.comparing(Wine::od280))
                .orElseThrow(NoSuchElementException::new);
        double maxod280 = maxod280Wine.od280();
        double minod280 = minod280Wine.od280();
        double normod280 = Math.pow(wine2.od280() - wine1.od280(), 2) / Math.pow(maxod280 - minod280, 2);

        // max/min prolines
        Wine maxprolWine = trainingset.stream().max(Comparator.comparing(Wine::proline))
                .orElseThrow(NoSuchElementException::new);
        Wine minprolWine = trainingset.stream().min(Comparator.comparing(Wine::proline))
                .orElseThrow(NoSuchElementException::new);
        double maxprol = maxprolWine.proline();
        double minprol = minprolWine.proline();
        double normprol = Math.pow(wine2.proline() - wine1.proline(), 2) / Math.pow(maxprol - minprol, 2);

        // total euclidean distance calculation
        dist = normalc + normal_acid + normash + normalc_ash + normmag + normphenols + normflava + normnonflava
                + normcyanins + normcolorint + normhue + normod280 + normprol;
        dist = Math.sqrt(dist);
        return dist;
    }
}