/**
 * i18n Tool
 * Web API of the i18n tool
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
import { BundleFileEntryDto } from './bundleFileEntryDto';

/**
 * Bundle file containing translations of keys.
 */
export interface BundleFileDto { 
    /**
     * Unique identifier of a bundle file.
     */
    id: string;
    /**
     * Name of this bundle file.
     */
    name: string;
    /**
     * Directory location of this bundle file.
     */
    location: string;
    /**
     * Type of bundle file
     */
    type: BundleFileDto.TypeDtoEnum;
    /**
     * All the file paths of this bundle.
     */
    files: Array<BundleFileEntryDto>;
}
export namespace BundleFileDto {
    export type TypeDtoEnum = 'JAVA_PROPERTIES' | 'JSON_ICU';
    export const TypeDtoEnum = {
        JAVAPROPERTIES: 'JAVA_PROPERTIES' as TypeDtoEnum,
        JSONICU: 'JSON_ICU' as TypeDtoEnum
    };
}
