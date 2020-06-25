package be.sgerard.i18n.model.i18n.persistence;

import be.sgerard.i18n.model.i18n.file.ScannedBundleFileEntry;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.UUID;

/**
 * File composing a {@link BundleFileEntity bundle file}.
 *
 * @author Sebastien Gerard
 */
@Entity(name = "bundle_file_entry")
public class BundleFileEntryEntity {

    @Id
    private String id;

    @ManyToOne(optional = false)
    private BundleFileEntity bundleFile;

    @ManyToOne(optional = false)
    private TranslationLocaleEntity locale;

    @NotNull
    @Column(nullable = false, columnDefinition = "TEXT")
    private String file;

    @Version
    private int version;

    BundleFileEntryEntity() {
    }

    public BundleFileEntryEntity(TranslationLocaleEntity locale, String file) {
        this.id = UUID.randomUUID().toString();
        this.locale = locale;
        this.file = file;
    }

    public BundleFileEntryEntity(ScannedBundleFileEntry entry) {
        this(entry.getLocale(), entry.getFile().toString());
    }

    /**
     * Returns the unique id of this bundle file entry.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique id of this bundle file entry.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the {@link BundleFileEntity bundle file} owning this entry.
     */
    public BundleFileEntity getBundleFile() {
        return bundleFile;
    }

    /**
     * Sets the {@link BundleFileEntity bundle file} owning this entry.
     */
    public BundleFileEntryEntity setBundleFile(BundleFileEntity bundleFile) {
        this.bundleFile = bundleFile;
        return this;
    }

    /**
     * Returns the {@link TranslationLocaleEntity locale} of this file.
     */
    public TranslationLocaleEntity getLocale() {
        return locale;
    }

    /**
     * Sets the {@link TranslationLocaleEntity locale} of this file.
     */
    public BundleFileEntryEntity setLocale(TranslationLocaleEntity locale) {
        this.locale = locale;
        return this;
    }

    /**
     * Returns the file part of the bundle file.
     */
    public String getFile() {
        return file;
    }

    /**
     * Returns the file part of the bundle file.
     */
    public File getJavaFile() {
        return new File(getFile());
    }

    /**
     * Sets the file part of the bundle file.
     */
    public BundleFileEntryEntity setFile(String file) {
        this.file = file;
        return this;
    }
}