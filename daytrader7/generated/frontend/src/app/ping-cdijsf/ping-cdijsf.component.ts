import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { PingCDIJSFBeanApi } from '../api/ping-cdijsf-bean.api';

@Component({
  selector: 'app-ping-cdijsf',
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
          <td><span style="color: blue">Hit Count: {{ pingCDIJSFBean.hitCount }}</span></td>
        </tr>
      </tbody>
    </form>
  `,
})
export class PingCdijsfComponent implements OnInit {
  protected readonly form = this.fb.nonNullable.group({
  });


  protected pingCDIJSFBean: { hitCount: number | null } = { hitCount: null };

  constructor(
    private readonly fb: FormBuilder,
    private readonly pingCDIJSFBeanApi: PingCDIJSFBeanApi,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  /** Loads what the page reads (the JSF page-load, again after a command). */
  private load(): void {
    this.pingCDIJSFBeanApi.getHitCount().subscribe({
      next: (value) => { this.pingCDIJSFBean.hitCount = value;  },
      error: (error: unknown) => { console.error('pingCDIJSFBean.hitCount', error); },
    });
  }
}
