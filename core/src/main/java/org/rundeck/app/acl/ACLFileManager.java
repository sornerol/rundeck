package org.rundeck.app.acl;

import com.dtolabs.rundeck.core.authorization.RuleSetValidation;
import com.dtolabs.rundeck.core.authorization.providers.BaseValidator;
import com.dtolabs.rundeck.core.authorization.providers.PolicyCollection;
import com.dtolabs.rundeck.core.authorization.providers.Validator;
import com.dtolabs.rundeck.core.storage.StorageManagerListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface ACLFileManager {
    BaseValidator getValidator();

    /**
     * Receive notification of changes
     *
     * @param listener
     */
    default void addListener(ACLFileManagerListener listener){
        throw new UnsupportedOperationException("addListener");
    }

    /**
     * Remove a listener
     *
     * @param listener
     */
    default void removeListener(ACLFileManagerListener listener){
        throw new UnsupportedOperationException("removeListener");
    }
    /**
     * Store a system policy file
     *
     * @param fileName name without path
     * @param input input stream
     * @return size of bytes stored
     */
    long storePolicyFile(String fileName, InputStream input) throws IOException;

    /**
     * Delete a policy file
     *
     * @return true if successful
     */
    boolean deletePolicyFile(String fileName) throws IOException;

    /**
     * Store a system policy file
     *
     * @param fileName name without path
     * @param fileText contents
     * @return size of bytes stored
     */
    long storePolicyFileContents(String fileName, String fileText) throws IOException;

    /**
     * Retrieve a system policy
     *
     * @param fileName name without path
     * @return definition
     */
    AclPolicyFile getAclPolicy(String fileName);

    /**
     * @param fileName name of policy file, without path
     * @return text contents of the policy file
     */
    String getPolicyFileContents(String fileName) throws IOException;

    /**
     * Load content to output stream
     *
     * @param fileName name of policy file, without path
     * @return length of output
     */
    public long loadPolicyFileContents(String fileName, OutputStream outputStream) throws IOException;

    /**
     * @param file name without path
     * @return true if the policy file with the given name exists
     */
    boolean existsPolicyFile(String file);

    default RuleSetValidation<PolicyCollection> validatePolicyFile(String fname) throws IOException{
        String policyFileContents = getPolicyFileContents(fname);
        if (policyFileContents == null) {
            return null;
        }
        return getValidator().validateYamlPolicy(fname, policyFileContents);
    }

    /**
     * List the system aclpolicy file names, not including the dir path
     */
    List<String> listStoredPolicyFiles();
}
