import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-ping-jsf',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [ReactiveFormsModule],
  template: `
    <form [formGroup]="form" data-legacy-body>
      <!-- TODO(ui_text): head_not_carried:meta -->
      <!-- TODO(ui_text): head_not_carried:title -->
      <!-- TODO(ui_text): head_not_carried:link -->
      <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
      <tbody>
        <tr>
          <td>
            <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
            <tbody>
              <tr><!-- TODO(ui_text): static_attribute_not_carried:not_listed --><b>Quotes</b></tr>
              <tr>
                <!-- TODO(ui_text): static_attribute_not_carried:not_listed -->
                <!-- TODO(widget): h:dataTable bound to quotedata.quotes not emitted: repeat_source_unproven -->
              </tr>
            </tbody>
          </td>
        </tr>
      </tbody>
    </form>
  `,
})
export class PingJsfComponent {
  protected readonly form = this.fb.nonNullable.group({
  });


  constructor(private readonly fb: FormBuilder) {}
}
