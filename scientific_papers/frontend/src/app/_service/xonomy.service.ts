import { Injectable } from '@angular/core';
declare const Xonomy: any;

@Injectable({
    providedIn: 'root'
})
export class XonomyService {

    constructor() { }

    public scientificPaperElement = {
        elements: {
            scientific_paper: {
                menu: [{
                    caption: 'Add <head>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<head></head>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('head');
                    }
                }, {
                    caption: 'Add <body>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<body></body>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('body');
                    }
                }]
            },
            head: {
                mustBeBefore: ['body'],
                menu: [{
                    caption: 'Add <recieved_date>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<recieved_date></recieved_date>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('recieved_date');
                    }
                }, {
                    caption: 'Add <revised_date>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<revised_date></revised_date>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('revised_date');
                    }
                },
                {
                    caption: 'Add <accepted_date>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<accepted_date></accepted_date>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('accepted_date');
                    }
                },
                {
                    caption: 'Add <title>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<title lang=""></title>'
                },
                {
                    caption: 'Add <author>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<author></author>'
                },
                {
                    caption: 'Add <keyword>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<keyword></keyword>'
                }]
            },
            body: {
                mustBeAfter: ['head'],
                menu: [{
                    caption: 'Add <abstract>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<abstract></abstract>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('abstract');
                    }
                },
                {
                    caption: 'Add <chapter>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<chapter></chapter>'
                },
                {
                    caption: 'Add <references>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<references></references>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('references');
                    }
                },
                {
                    caption: 'Delete body',
                    action: Xonomy.deleteElement
                }],
            },
            recieved_date: {
                mustBeBefore: ['revised_date', 'accepted_date', 'title', 'author', 'keyword'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            revised_date: {
                oneliner: true,
                hasText: true,
                mustBeBefore: ['accepted_date', 'title', 'author', 'keyword'],
                mustBeAfter: ['recieved_date'],
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            accepted_date: {
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                mustBeBefore: ['title', 'author', 'keyword'],
                mustBeAfter: ['recieved_date', 'revised_date'],
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            title: {
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                mustBeBefore: ['author', 'keyword', 'reference_author', 'publisher', 'year_of_publication', 'pages'],
                mustBeAfter: ['recieved_date', 'revised_date', 'accepted_date'],
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
                attributes: {
                    lang: {
                        asker: Xonomy.askString
                    },
                }
            },
            author: {
                mustBeBefore: ['keyword'],
                mustBeAfter: ['title', 'recieved_date', 'revised_date', 'accepted_date'],
                menu: [{
                    caption: 'Add <first_name>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<first_name></first_name>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('first_name');
                    }
                },
                {
                    caption: 'Add <middle_name>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<middle_name></middle_name>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('middle_name');
                    }
                },
                {
                    caption: 'Add <last_name>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<last_name></last_name>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('last_name');
                    }
                },
                {
                    caption: 'Add <email>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<email></email>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('email');
                    }
                }
                    ,
                {
                    caption: 'Add <affiliation>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<affiliation></affiliation>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('affiliation');
                    }
                },
                {
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]

            },
            keyword: {
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                mustBeAfter: ['author', 'title', 'recieved_date', 'revised_date', 'accepted_date'],
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            affiliation: {
                menu: [{
                    caption: 'Add <name>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<name></name>',
                    mustBeAfter: ['email'],
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('name');
                    }
                },
                {
                    caption: 'Add <city>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<city></city>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('city');
                    }
                },
                {
                    caption: 'Add <state>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<state></state>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('state');
                    }
                },
                {
                    caption: 'Add <country>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<country></country>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('country');
                    }
                },
                {
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]

            },
            first_name: {
                mustBeBefore: ['middle_name', 'last_name', 'email', 'affiliation'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            middle_name: {
                mustBeBefore: ['last_name', 'email', 'affiliation'],
                mustBeAfter: ['first_name'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            last_name: {
                mustBeAfter: ['first_name', 'middle_name'],
                mustBeBefore: ['email', 'affiliation'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            email: {
                mustBeAfter: ['first_name', 'middle_name', 'last_name'],
                mustBeBefore: ['affiliation'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            name: {
                mustBeBefore: ['city', 'state', 'country'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            city: {
                mustBeBefore: ['state', 'country'],
                mustBeAfter: ['name'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            state: {
                mustBeBefore: ['country'],
                mustBeAfter: ['name', 'city'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            country: {
                mustBeAfter: ['name', 'city', 'state'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            abstract: {
                mustBeBefore: ['chapter', 'references'],
                menu: [{
                    caption: 'Add <paragraph>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<paragraph></paragraph>'
                },
                {
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]

            },
            paragraph: {
                mustBeAfter: ['heading'],
                oneliner: true,
                menu: [{
                    caption: 'Edit',
                    action: Xonomy.editRaw,
                    actionParameter: {
                        fromJs: (jsElement: any) => {
                            return jsElement.getText();
                        },
                        toJs: (txt: any, origElement: any) => {
                            origElement.addText(txt);
                            return origElement;
                        }
                    }
                },
                {
                    caption: 'Add <image >',
                    action: Xonomy.newElementChild,
                    actionParameter: '<image src="" width="" height=""></image>'
                },
                {
                    caption: 'Add <table>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<table></table>'
                },
                {
                    caption: 'Add <b>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<b></b>'
                },
                {
                    caption: 'Add <i>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<i></i>'
                },
                {
                    caption: 'Add <u>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<u></u>'
                },
                {
                    caption: 'Add <unordered_list>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<unordered_list style="circle"></unordered_list>'
                },
                {
                    caption: 'Add <ordered_list>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<ordered_list type="1"></ordered_list>'
                },
                {
                    caption: 'Add <quote>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<quote></quote>'
                },
                {
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }],
            },
            image: {
                menu: [
                    {
                        caption: 'Add <name>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<name></name>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('name');
                        }
                    },
                    {
                        caption: 'Add <description>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<description></description>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('description');
                        }
                    }],
                attributes: {
                    src: {
                        asker: Xonomy.askString
                    },
                    width: {
                        asker: Xonomy.askString
                    },
                    height: {
                        asker: Xonomy.askString
                    }
                }
            },
            table: {
                menu: [
                    {
                        caption: 'Add <name>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<name></name>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('name');
                        }
                    },
                    {
                        caption: 'Add <description>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<description></description>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('description');
                        }
                    },
                    {
                        caption: 'Add <tr>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<tr></tr>'
                    }]
            },
            description: {
                hasText: true,
                asker: Xonomy.askString,
                mustBeAfter: ['name'],
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
            tr: {
                hasText: true,
                asker: Xonomy.askString,
                mustBeAfter: ['description', 'tr'],
                menu: [
                    {
                        caption: 'Add <th>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<th></th>'
                    },
                    {
                        caption: 'Add <td>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<td></td>'
                    },
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
            th: {
                mustBeBefore: ['td'],
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
            td: {
                mustBeAfter: ['th'],
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
            b: {
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Add <i>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<i></i>'
                    },
                    {
                        caption: 'Add <u>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<u></u>'
                    },
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
            u: {
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Add <i>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<i></i>'
                    },
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
            unordered_list: {
                menu: [
                    {
                        caption: 'Add <item>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<item></item>'
                    },
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
                attributes: {
                    style: {
                        asker: Xonomy.askPicklist,
                        askerParameter: [
                            'disc', 'circle', 'square', 'none'
                        ]
                    }
                }
            },
            ordered_list: {
                menu: [
                    {
                        caption: 'Add <item>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<item></item>'
                    },
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
                attributes: {
                    style: {
                        asker: Xonomy.askPicklist,
                        askerParameter: [
                            '1', 'A', 'a', 'I', 'i'
                        ]
                    }
                }
            },
            item: {
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
            quote: {
                menu: [
                    {
                        caption: 'Add <source>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<source></source>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('source');
                        }
                    },
                    {
                        caption: 'Add <text>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<text></text>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('text');
                        }
                    },
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
            source: {
                mustBeBefore: ['text'],
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
            text: {
                mustBeAfter: ['source'],
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
            chapter: {
                mustBeAfter: ['abstract', 'heading'],
                mustBeBefore: ['references'],
                menu: [
                    {
                        caption: 'Add <heading>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<heading></heading>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('heading');
                        }
                    },
                    {
                        caption: 'Add <paragraph>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<paragraph></paragraph>'
                    },
                    {
                        caption: 'Add <chapter>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<chapter></chapter>'
                    },
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],

            },
            heading: {
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                mustBeBefore: ['chapter', 'paragraph'],
            },
            references: {
                mustBeAfter: ['chapter'],
                menu: [
                    {
                        caption: 'Add <reference>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<reference></reference>'
                    },
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],

            },
            reference: {
                menu: [
                     {
                        caption: 'Add <paper_id>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<paper_id></paper_id>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('paper_id');
                        }
                    },
                    {
                        caption: 'Add <title>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<title></title>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('title');
                        }
                    },
                    {
                        caption: 'Add <reference_author>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<reference_author></reference_author>'
                    },
                    {
                        caption: 'Add <publisher>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<publisher></publisher>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('publisher');
                        }
                    },
                    {
                        caption: 'Add <year_of_publication>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<year_of_publication></year_of_publication>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('year_of_publication');
                        }
                    },
                    {
                        caption: 'Add <pages>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<pages></pages>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('pages');
                        }
                    },
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],

            },
            paper_id: {
                oneliner: true,
                mustBeBefore: ['title', 'reference_author', 'publisher', 'year_of_publication', 'pages'],
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
            reference_author: {
                mustBeAfter: ['title', 'paper_id'],
                mustBeBefore: ['publisher', 'year_of_publication', 'pages'],
                menu: [
                    {
                        caption: 'Add <first_name>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<first_name></first_name>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('first_name');
                        }
                    },
                    {
                        caption: 'Add <middle_name>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<middle_name></middle_name>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('middle_name');
                        }
                    },
                    {
                        caption: 'Add <last_name>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<last_name></last_name>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('last_name');
                        }
                    },
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],

            },
            publisher: {
                oneliner: true,
                mustBeAfter: ['paper_id', 'title', 'reference_author'],
                mustBeBefore: ['year_of_publication', 'pages'],
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
            year_of_publication: {
                oneliner: true,
                mustBeAfter: ['paper_id', 'title', 'reference_author', 'publisher'],
                mustBeBefore: ['pages'],
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
            pages: {
                oneliner: true,
                mustBeAfter: ['paper_id', 'title', 'reference_author', 'publisher', 'year_of_publication'],
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }],
            },
        }
    };
}
