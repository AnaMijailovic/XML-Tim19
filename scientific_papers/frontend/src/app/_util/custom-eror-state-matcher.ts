import { ErrorStateMatcher } from '@angular/material';
import { FormControl, FormGroupDirective, NgForm } from '@angular/forms';

export class CustomErorStateMatcher implements ErrorStateMatcher {
    isErrorState(
        control: FormControl | null,
        form: FormGroupDirective | NgForm | null
    ): boolean {
        const invalidControl = !!(control && control.invalid && control.parent.dirty);
        const invalidParent = !!(
            control &&
            control.parent &&
            control.parent.invalid &&
            control.parent.dirty
        );
        return invalidControl || invalidParent;
    }
}
