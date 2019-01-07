package link.invalidation.metrics;

import link.invalidation.models.Pair;

import java.util.List;

public class Evaluator {

    private double truePositive = 0;
    private double falsePositive = 0;
    private double trueNegative = 0;
    private double falseNegative = 0;

    public void evaluate(List<Pair> correctPairs, List<Pair> predictions) {

        for (int i = 0; i < correctPairs.size(); i++) {

            Pair correctPair = correctPairs.get(i);
            Pair predictionPair = predictions.get(i);

            if (!correctPair.isValid() && !predictionPair.isValid()) {
                this.truePositive += 1;
            }

            if (correctPair.isValid() && predictionPair.isValid()) {
                this.trueNegative += 1;
            }

            if (correctPair.isValid() && !predictionPair.isValid()) {
                this.falsePositive += 1;
            }

            if (!correctPair.isValid() && predictionPair.isValid()) {
                this.falseNegative += 1;
            }
        }
    }

    public double precision() {
        return this.truePositive / (this.truePositive + this.falsePositive);
    }

    public double recall() {
        return this.truePositive / (this.truePositive + this.falseNegative);
    }

    public double accuracy() {
        return (this.trueNegative + this.truePositive) /
                (this.truePositive + this.trueNegative +
                        this.falseNegative + this.falsePositive);
    }

    public double F1Score() {
        double precision = this.precision();
        double recall = this.recall();
        return 2 * ((precision * recall) / (precision + recall));
    }
}
