import { Injectable } from '@angular/core';
declare const Xonomy: any;

@Injectable({
    providedIn: 'root'
})
export class XonomyReviewService {

    constructor() { }

    public reviewElement = {
        elements: {
            evaluation_form: {
                menu: [{
                    caption: 'Add <reviewer>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<reviewer></reviewer>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('reviewer');
                    }
                }, {
                    caption: 'Add <scientific_paper_title>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<scientific_paper_title></scientific_paper_title>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('scientific_paper_title');
                    }
                }, {
                    caption: 'Add <scientific_paper_summary>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<scientific_paper_summary></scientific_paper_summary>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('scientific_paper_summary');
                    }
                }, {
                    caption: 'Add <suggestions>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<suggestions></suggestions>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('suggestions');
                    }
                }, {
                    caption: 'Add <overall_recommendation>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<overall_recommendation></overall_recommendation>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('overall_recommendation');
                    }
                }]
            },
            reviewer: {
                menu: [{
                    mustBeBefore: ['scientific_paper_title', 'scientific_paper_summary', 'suggestions', 'overall_recommendation'],
                    menu: [{
                        caption: 'Add <first_name>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<first_name></first_name>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('first_name');
                        }
                    }, {
                        caption: 'Add <last_name>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<last_name></last_name>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('last_name');
                        }
                    }, {
                        caption: 'Add <email>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<email></email>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('email');
                        }
                    }, {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
                }]
            },
            scientific_paper_title: {
                menu: [{
                    mustBeBefore: ['scientific_paper_summary', 'suggestions', 'overall_recommendation'],
                    mustBeAfter: ['reviewer'],
                    oneliner: true,
                    hasText: true,
                    asker: Xonomy.askString,
                    menu: [{
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
                }]
            },
            scientific_paper_summary: {
                menu: [{
                    mustBeBefore: ['suggestions', 'overall_recommendation'],
                    mustBeAfter: ['reviewer', 'scientific_paper_title'],
                    oneliner: true,
                    hasText: true,
                    asker: Xonomy.askString,
                    menu: [{
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
                }]
            },
            suggestions: {
                menu: [{
                    mustBeBefore: ['overall_recommendation'],
                    mustBeAfter: ['reviewer', 'scientific_paper_title', 'scientific_paper_summary'],
                    menu: [{
                        caption: 'Add <suggestion>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<suggestion></suggestion>'
                    }, {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
                }]
            },
            overall_recommendation : {
                menu: [{
                    mustBeAfter: ['reviewer', 'scientific_paper_title', 'scientific_paper_summary', 'suggestions'],
                    menu: [{
                        caption: 'Add <recommendation>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<recommendation></recommendation>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('recommendation');
                        }
                    }, {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
                }]
            },
            first_name: {
                mustBeBefore: ['last_name', 'email'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [{
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]
            },
            last_name: {
                mustBeBefore: ['email'],
                mustBeAfter: ['first_name'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [{
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]
            },
            email: {
                mustBeAfter: ['first_name', 'last_name'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [{
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]
            },
            suggestion: {
                menu: [{
                    caption: 'Add <ref>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<ref></ref>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('ref');
                    }
                }, {
                    caption: 'Add <comment>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<comment></comment>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('comment');
                    }
                }]
            },
            recommendation: {
                mustBeBefore: ['recommendation_comment'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [{
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]
            },
            recommendation_comment: {
                mustBeAfter: ['recommendation'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [{
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]
            },
            ref: {
                mustBeBefore: ['comment'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [{
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]
            },
            comment: {
                mustBeAfter: ['ref'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [{
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]
            }
        }
    };
}
