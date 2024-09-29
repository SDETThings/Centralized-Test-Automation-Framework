package com.qa.component.API.Certificates;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.certificates.CertificateClient;
import com.azure.security.keyvault.certificates.CertificateClientBuilder;
import com.azure.security.keyvault.certificates.models.KeyVaultCertificateWithPolicy;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import io.restassured.config.SSLConfig;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

    public class ClientCertificate {
        public synchronized SSLConfig LoadPfxCertFromLocal(String pfxFilePath , String pfxPassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");
            FileInputStream clientStoreFile = new FileInputStream(pfxFilePath);
            clientStore.load(clientStoreFile,pfxPassword.toCharArray());
            org.apache.http.conn.ssl.SSLSocketFactory clientAuthFactory = new org.apache.http.conn.ssl.SSLSocketFactory(clientStore,pfxPassword);
            SSLConfig config = new SSLConfig().with().sslSocketFactory(clientAuthFactory).and().allowAllHostnames();

            return config;
        }
        public synchronized SSLConfig LoadPfxCertFromKeyVault(String keyVaultUrl, String pfxCertificateName, String pfxPassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
            DefaultAzureCredentialBuilder credentialBuilder = new DefaultAzureCredentialBuilder();
            CertificateClient certificateClient = new CertificateClientBuilder().vaultUrl(keyVaultUrl).credential(credentialBuilder.build()).buildClient();
            byte[] pfxCertificate = null;
            SSLConfig config;
            try{
                KeyVaultCertificateWithPolicy certificateWithPolicy =
                        certificateClient.getCertificate(pfxCertificateName);
                pfxCertificate = certificateWithPolicy.getCer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try
            {
                KeyStore clientStore = KeyStore.getInstance("PKCS12");
                clientStore.load(new ByteArrayInputStream(pfxCertificate) , pfxPassword.toCharArray());
                org.apache.http.conn.ssl.SSLSocketFactory clientAuthFactory = new org.apache.http.conn.ssl.SSLSocketFactory(clientStore,pfxPassword);
                config = new SSLConfig().with().sslSocketFactory(clientAuthFactory).and().allowAllHostnames();
            } catch (KeyStoreException | UnrecoverableKeyException | KeyManagementException |
                     CertificateException |
                     NoSuchAlgorithmException | IOException e) {
                throw new RuntimeException(e);
            }
            return config ;
        }
        public synchronized String LoadStringFromKeyVault(String keyVaultUri,String passwordKey) {
            SecretClient secretClient = new SecretClientBuilder().vaultUrl(keyVaultUri).credential(new DefaultAzureCredentialBuilder().build()).buildClient();
            KeyVaultSecret secretBundle1 = secretClient.getSecret(passwordKey);
            return secretBundle1.getValue();
        }

}
