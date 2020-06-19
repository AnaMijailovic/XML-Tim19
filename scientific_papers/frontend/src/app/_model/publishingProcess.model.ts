export interface PublishingProcess {
    processId: string;
    latestPaperId: string;
    latestCoverId: string;
    finishedReviewsIds: string[];
    titles: string[];
    authors: string[];
    editorUsername: string;
    editorName: string;
    reviewers: string[];
    reviewersIds: string[];
    status: string;
    version: string;
}
