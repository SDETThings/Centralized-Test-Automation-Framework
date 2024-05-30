package com.qa.Utils;

import com.qa.Base.BaseClass;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer extends BaseClass implements IRetryAnalyzer {
    private int maxRetryCount=0;
    private int retryCount=0;
    public boolean isFinalRetry() {
        if(retryCount==maxRetryCount)
        {
            return true;
        }else
        {
            return false;
        }
    }
    @Override
    public boolean retry(ITestResult iTestResult) {{
            if(retryCount<maxRetryCount)
            {
                retryCount++;
                //setRetryCount(retryCount);
                return true;
            }
            return false;
        }
    }
}
