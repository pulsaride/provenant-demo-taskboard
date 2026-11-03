// Shapes the REST endpoints exchange, as proven by the lowering (eir::TirDto).

export interface Task {
  id: number | null;
  version: number | null;
  createdDate: string | null;
  lastModifiedDate: string | null;
  createdBy: string | null;
  lastModifiedBy: string | null;
  name: string | null;
  description: string | null;
  status: Status | null;
}

export type Status = 'TODO' | 'DOING' | 'DONE';

export interface TaskHomeDoingtasksView {
  description: string | null;
  id: number | null;
  name: string | null;
}

export interface TaskHomeDonetasksView {
  description: string | null;
  id: number | null;
  name: string | null;
}

export interface TaskHomeTodotasksView {
  description: string | null;
  id: number | null;
  name: string | null;
}

export interface ViewTaskActionTaskView {
  createdDate: string | null;
  description: string | null;
  lastModifiedDate: string | null;
  name: string | null;
  status: Status | null;
}
