import { Injectable } from '@angular/core';
declare const Xonomy: any;

@Injectable({
    providedIn: 'root'
})
export class XonomyCoverLetterService {

    constructor() { }

    public coverLetterElement = {
        elements: {
            cover_letter: {
                menu: [{
                    caption: 'Add <cover_letter_header>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<cover_letter_header></cover_letter_header>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('cover_letter_header');
                    }
                }, {
                    caption: 'Add <sender>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<sender></sender>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('sender');
                    }
                }, {
                    caption: 'Add <recipient>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<recipient></recipient>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('recipient');
                    }
                }, {
                    caption: 'Add <date>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<date></date>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('date');
                    }
                }, {
                    caption: 'Add <cover_letter_body>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<cover_letter_body></cover_letter_body>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('cover_letter_body');
                    }
                }, {
                    caption: 'Add <cover_letter_closing>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<cover_letter_closing></cover_letter_closing>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('cover_letter_closing');
                    }
                }],
            },
            cover_letter_header: {
                mustBeBefore: ['sender', 'recipient', 'date', 'cover_letter_body', 'cover_letter_closing'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            sender: {
                mustBeBefore: ['recipient', 'date', 'cover_letter_body', 'cover_letter_closing'],
                mustBeAfter: ['cover_letter_header'],
                menu: [
                    {
                        caption: 'Add <sender_name>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<sender_name></sender_name>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('sender_name');
                        }
                    },
                    {
                        caption: 'Add <university_name>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<university_name></university_name>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('university_name');
                        }
                    },
                    {
                        caption: 'Add <university_address>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<university_address></university_address>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('university_address');
                        }
                    },
                    {
                        caption: 'Add <phone_number>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<phone_number></phone_number>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('phone_number');
                        }
                    },
                    {
                        caption: 'Add <email>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<email></email>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('email');
                        }
                    },
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            recipient: {
                mustBeBefore: ['date', 'cover_letter_body', 'cover_letter_closing'],
                mustBeAfter: ['cover_letter_header', 'sender'],
                menu: [
                    {
                        caption: 'Add <recipient_name>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<recipient_name></recipient_name>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('recipient_name');
                        }
                    },
                    {
                        caption: 'Add <recipient_role>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<recipient_role></recipient_role>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('recipient_role');
                        }
                    },
                    {
                        caption: 'Add <journal_name>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<journal_name></journal_name>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('journal_name');
                        }
                    },
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            date: {
                mustBeBefore: ['cover_letter_body', 'cover_letter_closing'],
                mustBeAfter: ['cover_letter_header', 'sender', 'recipient'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            cover_letter_body: {
                mustBeBefore: ['cover_letter_closing'],
                mustBeAfter: ['cover_letter_header', 'sender', 'recipient', 'date'],
                hasText: true,
                menu: [
                    {
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
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            cover_letter_closing: {
                mustBeAfter: ['cover_letter_header', 'sender', 'recipient', 'date', 'cover_letter_body'],
                menu: [{
                    caption: 'Add <signature>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<signature></signature>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('signature');
                    }
                },
                {
                    caption: 'Add <sender_name>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<sender_name></sender_name>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('sender_name');
                    }
                },
                {
                    caption: 'Add <academic_title>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<academic_title></academic_title>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('academic_title');
                    }
                },
                {
                    caption: 'Add <department_name>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<department_name></department_name>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('department_name');
                    }
                }
                    ,
                {
                    caption: 'Add <university_name>',
                    action: Xonomy.newElementChild,
                    actionParameter: '<university_name></university_name>',
                    hideIf: (jsElement: any) => {
                        return jsElement.hasChildElement('university_name');
                    }
                },
                {
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]

            },
            sender_name: {
                mustBeBefore: ['university_name', 'university_address', 'phone_number', 'email',
                               'academic_title', 'department_name', 'university_name'],
                mustBeAfter: ['signature'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            university_name: {
                mustBeBefore: ['university_name', 'university_address', 'phone_number', 'email'],
                mustBeAfter: ['sender_name', 'signature', 'sender_name', 'academic_title', 'department_name'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            university_address: {
                mustBeBefore: ['phone_number', 'email'],
                mustBeAfter: ['sender_name', 'university_name'],
                menu: [
                    {
                        caption: 'Add <street>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<street></street>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('street');
                        }
                    }
                    ,
                    {
                        caption: 'Add <street_number>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<street_number></street_number>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('street_number');
                        }
                    }
                    ,
                    {
                        caption: 'Add <city>',
                        action: Xonomy.newElementChild,
                        actionParameter: '<city></city>',
                        hideIf: (jsElement: any) => {
                            return jsElement.hasChildElement('city');
                        }
                    }
                        ,
                        {
                            caption: 'Add <country>',
                            action: Xonomy.newElementChild,
                            actionParameter: '<country></country>',
                            hideIf: (jsElement: any) => {
                                return jsElement.hasChildElement('country');
                            }
                        }
                            ,
                            {
                                caption: 'Add <postal_number>',
                                action: Xonomy.newElementChild,
                                actionParameter: '<postal_number></postal_number>',
                                hideIf: (jsElement: any) => {
                                    return jsElement.hasChildElement('postal_number');
                                }
                            }
                                ,
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            phone_number: {
                mustBeAfter: ['sender_name', 'university_name', 'university_address'],
                mustBeBefore: ['email'],
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
                mustBeAfter: ['sender_name', 'university_name', 'university_address', 'phone_number'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            street: {
                mustBeBefore: ['street_number', 'city', 'country', 'postal_number'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            street_number: {
                mustBeBefore: ['city', 'country', 'postal_number'],
                mustBeAfter: ['street'],
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
                mustBeBefore: ['country', 'postal_number'],
                mustBeAfter: ['street', 'street_number'],
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
                mustBeBefore: ['postal_number'],
                mustBeAfter: ['street', 'street_number', 'city'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            postal_number: {
                mustBeAfter: ['street', 'street_number', 'city', 'country'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                {
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]

            },
            signature: {
                mustBeBefore: ['sender_name', 'academic_title', 'department_name', 'university_name'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                {
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]

            }
            ,
            academic_title: {
                mustBeBefore: ['department_name', 'university_name'],
                mustBeAfter: ['signature', 'sender_name'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                {
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]

            }
            ,
            department_name: {
                mustBeBefore: ['university_name'],
                mustBeAfter: ['signature', 'sender_name', 'academic_title'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                {
                    caption: 'Delete element',
                    action: Xonomy.deleteElement
                }]

            },
            recipient_name: {
                mustBeBefore: ['recipient_role', 'journal_name'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            recipient_role: {
                mustBeBefore: ['journal_name'],
                mustBeAfter: ['recipient_name'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
            journal_name: {
                mustBeAfter: ['recipient_name', 'recipient_role'],
                oneliner: true,
                hasText: true,
                asker: Xonomy.askString,
                menu: [
                    {
                        caption: 'Delete element',
                        action: Xonomy.deleteElement
                    }]
            },
        }

    };
}
