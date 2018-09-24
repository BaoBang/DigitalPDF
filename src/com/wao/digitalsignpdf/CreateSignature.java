package com.wao.digitalsignpdf;

import com.wao.digitalsignpdf.errorexception.SignFailedException;
import com.wao.digitalsignpdf.utils.MessageConstant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.util.Calendar;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.ExternalSigningSupport;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;

/**
 * An example for signing a PDF with bouncy castle. A keystore can be created
 * with the java keytool, for example:
 *
 * {@code keytool -genkeypair -storepass 123456 -storetype pkcs12 -alias test -validity 365
 *        -v -keyalg RSA -keystore keystore.p12 }
 *
 * @author Thomas Chojecki
 * @author Vakhtang Koroghlishvili
 * @author John Hewson
 */
public class CreateSignature extends CreateSignatureBase {

    /**
     * Initialize the signature creator with a keystore and certficate password.
     *
     * @param keystore the pkcs12 keystore containing the signing certificate
     * @param alias the alias for the key
     * @param pin the password for recovering the key
     * @throws SignFailedException if the keystore has not been initialized
     * (loaded)
     */
    public CreateSignature(KeyStore keystore, String alias, char[] pin) throws SignFailedException {
        super(keystore, alias, pin);
    }

    /**
     * Signs the given PDF file. Alters the original file on disk.
     *
     * @param file the PDF file to sign
     * @throws SignFailedException if the file could not be read or written
     */
    public void signDetached(File file) throws SignFailedException {
        signDetached(file, file, null);
    }

    /**
     * Signs the given PDF file.
     *
     * @param inFile input PDF file
     * @param outFile output PDF file
     * @throws SignFailedException if the input file could not be read
     */
    public void signDetached(File inFile, File outFile) throws SignFailedException {
        signDetached(inFile, outFile, null);
    }

    /**
     * Signs the given PDF file.
     *
     * @param inFile input PDF file
     * @param outFile output PDF file
     * @param tsaUrl optional TSA url
     * @throws com.wao.digitalsignpdf.errorexception.SignFailedException
     */
    public void signDetached(File inFile, File outFile, String tsaUrl) throws SignFailedException {
        if (inFile == null || !inFile.exists()) {
            throw new SignFailedException(MessageConstant.FILE_NOT_EXITS_EXCEPTION);
        }
        setTsaUrl(tsaUrl);
        try (FileOutputStream fos = new FileOutputStream(outFile);
                PDDocument doc = PDDocument.load(inFile);) {

            // sign
            signDetached(doc, fos);
        } catch (Exception e) {
            throw new SignFailedException(MessageConstant.SIGN_FILE_FAILED_EXCEPTION, e);
        }
    }

    public void signDetached(PDDocument document, OutputStream output)
            throws IOException {
        int accessPermissions = SigUtils.getMDPPermission(document);
        if (accessPermissions == 1) {
            throw new IllegalStateException("No changes to the document are permitted due to DocMDP transform parameters dictionary");
        }

        // create signature dictionary
        PDSignature signature = new PDSignature();
        signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
        signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
        signature.setName("Wao");
        signature.setLocation("72, Trần Trọng Cung, Hồ Chí Minh, Việt Nam");
        signature.setReason("Digital sign pdf");
        // TODO extract the above details from the signing certificate? Reason as a parameter?

        // the signing date, needed for valid signature
        signature.setSignDate(Calendar.getInstance());

        // Optional: certify 
        if (accessPermissions == 0) {
            SigUtils.setMDPPermission(document, signature, 2);
        }

        if (isExternalSigning()) {
            System.out.println("Sign externally...");
            document.addSignature(signature);
            ExternalSigningSupport externalSigning
                    = document.saveIncrementalForExternalSigning(output);
            // invoke external signature service
            byte[] cmsSignature = sign(externalSigning.getContent());
            // set signature bytes received from the service
            externalSigning.setSignature(cmsSignature);
        } else {
            SignatureOptions signatureOptions = new SignatureOptions();
            // Size can vary, but should be enough for purpose.
            signatureOptions.setPreferredSignatureSize(SignatureOptions.DEFAULT_SIGNATURE_SIZE * 2);
            // register signature dictionary and sign interface
            document.addSignature(signature, this, signatureOptions);

            // write incremental (only for signing purpose)
            document.saveIncremental(output);
        }
    }
//
//    public static void main(String[] args) throws IOException, GeneralSecurityException {
//        if (args.length < 3) {
//            usage();
//            System.exit(1);
//        }
//
//        String tsaUrl = null;
//        boolean externalSig = false;
//        for (int i = 0; i < args.length; i++) {
//            System.out.println(Arrays.toString(args));
//            if (args[i].equals("-tsa")) {
//                i++;
//                if (i >= args.length) {
//                    usage();
//                    System.exit(1);
//                }
//                tsaUrl = args[i];
//            }
//            if (args[i].equals("-e")) {
//                externalSig = true;
//            }
//        }
//
//        // load the keystore
//        KeyStore keystore = KeyStore.getInstance("Windows-MY");
//        try {
//            keystore.load(null, null);
//        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        // TODO Auto-generated catch block
//        // TODO Auto-generated catch block
//        //KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//        // TODO alias command line argument
//        
//        //KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//        // TODO alias command line argument
//
//        // sign PDF
//        CreateSignature signing = new CreateSignature(keystore,"", "12".toCharArray());
//        signing.setExternalSigning(externalSig);
//
//        File inFile = new File(args[2]);
//        String name = inFile.getName();
//        String substring = name.substring(0, name.lastIndexOf('.'));
//
//        File outFile = new File(inFile.getParent(), substring + "_signed.pdf");
//        signing.signDetached(inFile, outFile, tsaUrl);
//    }

    private static void usage() {
        System.err.println("usage: java " + CreateSignature.class.getName() + " "
                + "<pkcs12_keystore> <password> <pdf_to_sign>\n" + ""
                + "options:\n"
                + "  -tsa <url>    sign timestamp using the given TSA server\n"
                + "  -e            sign using external signature creation scenario");
    }
}
