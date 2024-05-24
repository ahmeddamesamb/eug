import dayjs from 'dayjs/esm';
import { ITypeOperation } from 'app/entities/microserviceGIR/type-operation/type-operation.model';

export interface IOperation {
  id: number;
  dateExecution?: dayjs.Dayjs | null;
  emailUser?: string | null;
  detailOperation?: string | null;
  infoSysteme?: string | null;
  typeOperation?: Pick<ITypeOperation, 'id'> | null;
}

export type NewOperation = Omit<IOperation, 'id'> & { id: null };
