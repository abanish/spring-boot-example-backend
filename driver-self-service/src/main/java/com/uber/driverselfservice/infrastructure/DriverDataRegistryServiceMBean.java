package com.uber.driverselfservice.infrastructure;

public interface DriverDataRegistryServiceMBean {

    public int getSuccessfullAttemptsCounter();
    public int getUnuccessfullAttemptsCounter();
    public int getFallbackMethodExecutionCounter();
    
    public void resetCounters();
}