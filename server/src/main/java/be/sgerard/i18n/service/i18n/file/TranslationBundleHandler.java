package be.sgerard.i18n.service.i18n.file;

import be.sgerard.i18n.model.i18n.file.ScannedBundleFileDto;
import be.sgerard.i18n.model.i18n.file.ScannedBundleFileKeyDto;
import be.sgerard.i18n.service.git.RepositoryAPI;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Sebastien Gerard
 */
public interface TranslationBundleHandler {

    boolean support(ScannedBundleFileDto bundleFile);

    boolean continueScanning(File directory);

    Stream<ScannedBundleFileDto> scanBundles(File directory, RepositoryAPI repositoryAPI) throws IOException;

    Collection<ScannedBundleFileKeyDto> scanKeys(ScannedBundleFileDto bundleFile, RepositoryAPI repositoryAPI) throws IOException;

    void updateBundle(ScannedBundleFileDto bundleFile,
                      Collection<ScannedBundleFileKeyDto> keys,
                      RepositoryAPI repositoryAPI) throws IOException;

}