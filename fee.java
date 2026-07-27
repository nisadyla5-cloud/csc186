public class Fee {
    private double schoolFee;
    private double PIBGfee;
    private double tuitionFee;

    public Fee(double schoolFee, double PIBGfee, double tuitionFee) {
        this.schoolFee = schoolFee;
        this.PIBGfee = PIBGfee;
        this.tuitionFee = tuitionFee;
    }

    // Setters
    public void setSchoolFee(double schoolFee) {
        this.schoolFee = schoolFee;
    }

    public void setPIBGfee(double PIBGfee) {
        this.PIBGfee = PIBGfee;
    }

    public void setTuitionFee(double tuitionFee) {
        this.tuitionFee = tuitionFee;
    }

    // Getters
    public double getSchoolFee() {
        return schoolFee;
    }

    public double getPIBGfee() {
        return PIBGfee;
    }

    public double getTuitionFee() {
        return tuitionFee;
    }

    // Calculate total
    public double calcTotalFee() {
        return schoolFee + PIBGfee + tuitionFee;
    }

    // Proper toString
    @Override
    public String toString() {
        return "╔═══════════════════════════════════════╗\n" +
               "Student Name : Ali\n" +
               "MyKid Number : 070225120868\n" +
               "╠═══════════════════════════════════════╣\n" +
               "School Fee   : RM " + schoolFee + "\n" +
               "PIBG Fee     : RM " + PIBGfee + "\n" +
               "Tuition Fee  : RM " + tuitionFee + "\n" +
               "╠═══════════════════════════════════════╣\n" +
               "TOTAL        : RM " + calcTotalFee() + "\n" +
               "╚═══════════════════════════════════════╝";
    }
}
