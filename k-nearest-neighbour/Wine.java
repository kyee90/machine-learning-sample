public record Wine(double alc, double mal_acid, double ash, double alc_ash, double mag, double phenols,
        double flava, double nonflava, double cyanins, double colorint, double hue, double od280, double proline,
        int idclass) {

    public double distance(Wine wine) {
        double dist;
        dist = Math.pow(this.alc - wine.alc(), 2) + Math.pow(this.mal_acid - wine.mal_acid(), 2)
                + Math.pow(this.ash - wine.ash(), 2) + Math.pow(this.alc_ash - wine.alc_ash(), 2)
                + Math.pow(this.mag - wine.mag(), 2) + Math.pow(this.phenols - wine.phenols(), 2)
                + Math.pow(this.phenols - wine.phenols(), 2) + Math.pow(this.flava - wine.flava(), 2)
                + Math.pow(this.nonflava - wine.nonflava(), 2) + Math.pow(this.cyanins - wine.cyanins(), 2)
                + Math.pow(this.colorint - wine.colorint(), 2) + Math.pow(this.hue - wine.hue(), 2)
                + Math.pow(this.od280 - wine.od280(), 2) + Math.pow(this.proline - wine.proline(), 2);
        dist = Math.sqrt(dist);
        System.out.println(dist);
        return dist;
    }

    public double normdistance(Wine wine) {
        double dist;
        double normalc = Math.pow(this.alc - wine.alc(), 2) / Math.pow(14.83 - 11.45, 2);

        double normal_acid = Math.pow(this.mal_acid - wine.mal_acid(), 2);
        double normash = Math.pow(this.ash - wine.ash(), 2);
        double normalc_ash = Math.pow(this.alc_ash - wine.alc_ash(), 2);
        double normmag = Math.pow(this.mag - wine.mag(), 2);
        double normphenols = Math.pow(this.phenols - wine.phenols(), 2);
        double normflava = Math.pow(this.flava - wine.flava(), 2);
        double normnonflava = Math.pow(this.nonflava - wine.nonflava(), 2);
        double normcyanins = Math.pow(this.cyanins - wine.cyanins(), 2);
        double normcolorint = Math.pow(this.colorint - wine.colorint(), 2);
        double normhue = Math.pow(this.hue - wine.hue(), 2);
        double normod280 = Math.pow(this.od280 - wine.od280(), 2);
        double normprol = Math.pow(this.proline - wine.proline(), 2);
        dist = normalc + normal_acid + normash + normalc_ash + normmag + normphenols + normflava + normnonflava
                + normcyanins + normcolorint + normhue + normod280 + normprol;
        dist = Math.sqrt(dist);
        System.out.println("DISTANCE: " + dist);
        return dist;
    }
}