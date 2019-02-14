package org.auth1.auth1.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Auth1Configuration {

    public enum HashAlgorithm {
        BCRYPT,
        SCRYPT
    }

    private final HashAlgorithm hashAlgorithm;

    private final int bcryptCost;

    private final int scryptCost;
    private final int scryptBlockSize;
    private final int scryptParallelization;

    public Auth1Configuration(HashAlgorithm hashAlgorithm, int bcryptCost, int scryptCost, int scryptBlockSize, int scryptParallelization) {
        this.hashAlgorithm = hashAlgorithm;
        this.bcryptCost = bcryptCost;
        this.scryptCost = scryptCost;
        this.scryptBlockSize = scryptBlockSize;
        this.scryptParallelization = scryptParallelization;
    }

    public HashAlgorithm getHashAlgorithm() {
        return hashAlgorithm;
    }

    private void require(HashAlgorithm algorithm) {
        if (this.hashAlgorithm != algorithm) {
            throw new RuntimeException(String.format("Cannot get this value, current algorithm is \"%s\" not \"%s\"",
                    this.hashAlgorithm, algorithm));
        }
    }

    public int getBcryptCost() {
        require(HashAlgorithm.BCRYPT);
        return bcryptCost;
    }

    public int getScryptCost() {
        require(HashAlgorithm.SCRYPT);
        return scryptCost;
    }

    public int getScryptBlockSize() {
        require(HashAlgorithm.SCRYPT);
        return scryptBlockSize;
    }

    public int getScryptParallelization() {
        require(HashAlgorithm.SCRYPT);
        return scryptParallelization;
    }

    /**
     * Load a full {@link Auth1Configuration} from the given input. This method checks the validity of the file and throws
     * an InvalidConfigurationException with a detailed error method if the input is semantically invalid or malformed.
     *
     * @param input a stream containing the data for the properties file.
     * @return a new {@link Auth1Configuration} loaded with the auth1 configuration parameters.
     * @throws IOException if the input stream could not be loaded.
     * @throws Configuration.InvalidConfigurationException if the configuration file is invalid or malformed.
     */
    public static Auth1Configuration fromConfigurationFile(InputStream input) throws IOException, Configuration.InvalidConfigurationException {
        var properties = new Properties();
        properties.load(input);

        String algorithmName = Configuration.getRequiredProperty(properties, "HASH_ALGORITHM");
        final HashAlgorithm hashAlgorithm;
        if (algorithmName.equalsIgnoreCase("bcrypt")) {
            hashAlgorithm = HashAlgorithm.BCRYPT;
        } else if (algorithmName.equalsIgnoreCase("scrypt")) {
            hashAlgorithm = HashAlgorithm.SCRYPT;
        } else {
            throw new Configuration.InvalidConfigurationException("Invalid algorithm \"" + algorithmName + "\"."
                    + " Valid options are \"bcrypt\" and \"scrypt\"");
        }

        final int bcryptCost;
        final int scryptCost;
        final int scryptBlockSize;
        final int scryptParallelization;

        try {
            if (hashAlgorithm == HashAlgorithm.BCRYPT) {
                bcryptCost = Integer.valueOf(Configuration.getRequiredProperty(properties, "BCRYPT_COST"));
                scryptCost = -1;
                scryptBlockSize = -1;
                scryptParallelization = -1;
            } else {
                bcryptCost = -1;
                scryptCost = Integer.valueOf(Configuration.getRequiredProperty(properties, "SCRYPT_COST"));
                scryptBlockSize = Integer.valueOf(Configuration.getRequiredProperty(properties, "SCRYPT_BLOCK_SIZE"));
                scryptParallelization = Integer.valueOf(Configuration.getRequiredProperty(properties, "SCRYPT_PARALLELIZATION"));
            }
        } catch (NumberFormatException e) {
            throw new Configuration.InvalidConfigurationException(e.getMessage());
        }

        return new Auth1Configuration(hashAlgorithm, bcryptCost, scryptCost, scryptBlockSize, scryptParallelization);
    }
}
