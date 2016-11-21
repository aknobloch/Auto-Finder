package autofinder.aaronknobloch.com.autofinder;

/**
 * Created by aaron on 8/5/15.
 */
public class WarrantyCalculator {

    private String model;
    private int year;
    private int mileage;
    private int monthsLeftMileage;
    private int monthsLeftYear;

    public WarrantyCalculator(String inYear, String inModel, String inMileage) {

        // get model
        inModel = inModel.toLowerCase();
        if(inModel.contains("mitsubishi")) model = "mitsubishi";
        else if(inModel.contains("ford")) model = "ford";
        else if(inModel.contains("honda")) model = "honda";
        else if(inModel.contains("chevrolet")) model = "chevorlet";
        else if(inModel.contains("toyota")) model = "toyota";
        else if(inModel.contains("hyundai")) model = "hyundai";
        else if(inModel.contains("dodge")) model = "dodge";
        else if(inModel.contains("kia")) model = "kia";
        else if(inModel.contains("subaru")) model = "subaru";
        else if(inModel.contains("nissan")) model = "nissan";
        else if(inModel.contains("mazda")) model = "mazda";
        else if(inModel.contains("volkswagen")) model = "volkswagen";
        else if(inModel.contains("suzuki")) model = "suzuki";
        else if(inModel.contains("jeep")) model = "jeep";
        else if(inModel.contains("cooper")) model = "mini cooper";
        else if(inModel.contains("scion")) model = "scion";
        else {
            System.out.println("Could not find model for " + inModel +
                    " while searching for warranty.");
            model = "unknown";
        }

        // get year
        year = Integer.valueOf(inYear);


        // get mileage
        inMileage = inMileage.replace(",", "");
        try {
            mileage = Integer.valueOf(inMileage);
        } catch(NumberFormatException nfe) {
            System.out.println("Number Format Exception caught on '" + inMileage + "'. Set mileage to zero and continued.");
            mileage = 0;
        }


        calculateMonthsLeftMileage();
        calculateMonthsLeftYear();

    }

    private void calculateMonthsLeftMileage() {

        int mileageExpiration;

        if(model.equals("unknown")) mileageExpiration = 0;
        else if(model.equals("mitsubishi")) mileageExpiration = 60000;
        else if(model.equals("hyundai")) mileageExpiration = 60000;
        else if(model.equals("kia")) mileageExpiration = 60000;
        else if(model.equals("mini cooper")) mileageExpiration = 50000;
        else mileageExpiration = 36000;

        int daysLeft = (mileageExpiration - mileage) / 23;
        monthsLeftMileage = daysLeft / 30;

    }

    private void calculateMonthsLeftYear() {

        int yearExpiration;

        if(model.equals("unknown")) yearExpiration = 2015;
        else if(model.equals("mitsubishi")) yearExpiration = year + 5;
        else if(model.equals("hyundai")) yearExpiration = year + 5;
        else if(model.equals("kia")) yearExpiration = year + 5;
        else if(model.equals("mini cooper")) yearExpiration = year + 4;
        else yearExpiration = year + 3;

        monthsLeftYear = (yearExpiration - 2015) * 12;

    }

    public int monthsLeftOnWarranty() {
        return Math.min(monthsLeftYear, monthsLeftMileage);
    }

}
