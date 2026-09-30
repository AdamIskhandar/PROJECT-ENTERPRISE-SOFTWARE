package com.cliniccare.model;

public class MedicalService {

    private int serviceId;
    private String serviceName;
    private String description;
    private double price;

    public MedicalService() {
    }

    public MedicalService(String serviceName, String description, double price) {
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
    }

    public MedicalService(int serviceId, String serviceName,
                          String description, double price) {
        this(serviceName, description, price);
        this.serviceId = serviceId;
    }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
