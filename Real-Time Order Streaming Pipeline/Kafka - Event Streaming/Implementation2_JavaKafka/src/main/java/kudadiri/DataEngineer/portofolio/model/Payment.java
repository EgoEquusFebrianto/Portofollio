package kudadiri.DataEngineer.portofolio.model;

public class Payment {
    private String paymentType;
    private boolean success;

    public Payment() {}

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
