package com.wao.digitalsign.pdf;

import com.wao.digitalsign.errorexception.SignFailedException;
import com.wao.digitalsign.utils.MessageConstant;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

public abstract class CreateSignatureBase implements SignatureInterface {

    private PrivateKey privateKey;
    private Certificate[] certificateChain;
    private String tsaUrl;
    private boolean externalSigning;

    /**
     * Initialize the signature creator with a keystore (pkcs12) and pin that
     * should be used for the signature.
     *
     * @param keystore is a pkcs12 keystore.
     * @param alias is the alias of key
     * @param pin is the pin for the keystore / private key
     *
     * @throws SignFailedException if no certificate could be found
     */
    public CreateSignatureBase(KeyStore keystore, String alias, char[] pin) throws SignFailedException {
        try {
            Certificate cert = null;
            setPrivateKey((PrivateKey) keystore.getKey(alias, pin));
            Certificate[] certChain = keystore.getCertificateChain(alias);
            setCertificateChain(certChain);
            cert = certChain[0];
            if (cert instanceof X509Certificate) {
                // avoid expired certificate
                ((X509Certificate) cert).checkValidity();
            }
            // end test
            if (cert == null) {
                throw new SignFailedException(MessageConstant.NOT_FIND_CERTIFICATE_EXCEPTION);
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | CertificateExpiredException | CertificateNotYetValidException e) {
            throw new SignFailedException(MessageConstant.SIGN_FILE_FAILED_EXCEPTION, e);
        }
    }

    public final void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public final void setCertificateChain(final Certificate[] certificateChain) {
        this.certificateChain = certificateChain;
    }

    public void setTsaUrl(String tsaUrl) {
        this.tsaUrl = tsaUrl;
    }

    /**
     * SignatureInterface implementation.
     *
     * This method will be called from inside of the pdfbox and create the PKCS
     * #7 signature. The given InputStream contains the bytes that are given by
     * the byte range.
     *
     * This method is for internal use only.
     *
     * Use your favorite cryptographic library to implement PKCS #7 signature
     * creation.
     *
     * @throws IOException
     */
    @Override
    public byte[] sign(InputStream content) throws IOException {
        // cannot be done private (interface)
        try {
            List<Certificate> certList = new ArrayList<>();
            certList.addAll(Arrays.asList(certificateChain));
            Store certs = new JcaCertStore(certList);
            CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
            org.bouncycastle.asn1.x509.Certificate cert = org.bouncycastle.asn1.x509.Certificate
                    .getInstance(certificateChain[0].getEncoded());
            ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA256WithRSA").build(privateKey);
            gen.addSignerInfoGenerator(
                    new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().build())
                            .build(sha1Signer, new X509CertificateHolder(cert)));
            gen.addCertificates(certs);
            CMSProcessableInputStream msg = new CMSProcessableInputStream(content);
            CMSSignedData signedData = gen.generate(msg, false);
            if (tsaUrl != null && tsaUrl.length() > 0) {
                ValidationTimeStamp validation = new ValidationTimeStamp(tsaUrl);
                signedData = validation.addSignedTimeStamp(signedData);
            }
            return signedData.getEncoded();
        } catch (GeneralSecurityException | CMSException | OperatorCreationException e) {
            throw new IOException(e);
        }
    }

    /**
     * Set if external signing scenario should be used. If {@code false},
     * SignatureInterface would be used for signing.
     * <p>
     * Default: {@code false}
     * </p>
     *
     * @param externalSigning {@code true} if external signing should be
     * performed
     */
    public void setExternalSigning(boolean externalSigning) {
        this.externalSigning = externalSigning;
    }

    public boolean isExternalSigning() {
        return externalSigning;
    }
}
