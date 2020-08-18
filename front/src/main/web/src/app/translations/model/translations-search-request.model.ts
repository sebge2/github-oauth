import {Workspace} from "./workspace/workspace.model";
import {TranslationsSearchCriterion} from "./translations-search-criterion.model";
import {TranslationLocale} from "./translation-locale.model";

export class TranslationsSearchRequest {

    public workspaces: Workspace[] = [];
    public locales: TranslationLocale[] = [];
    public criterion: TranslationsSearchCriterion = TranslationsSearchCriterion.ALL;

    constructor() {
    }
}
