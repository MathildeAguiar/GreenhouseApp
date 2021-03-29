import { IUser } from 'app/entities/user/user.model';
import { ITask } from 'app/entities/task/task.model';
import { IReport } from 'app/entities/report/report.model';
import { IGreenHouse } from 'app/entities/green-house/green-house.model';
import { Category } from 'app/entities/enumerations/category.model';
import { Filiere } from 'app/entities/enumerations/filiere.model';

export interface IProfile {
  id?: number;
  name?: string;
  status?: Category;
  specialite?: Filiere | null;
  address?: string | null;
  codeP?: string | null;
  ville?: string | null;
  phoneNumber?: string | null;
  email?: string | null;
  indicatifContentType?: string | null;
  indicatif?: string | null;
  user?: IUser | null;
  works?: ITask[] | null;
  documents?: IReport[] | null;
  houses?: IGreenHouse[] | null;
}

export class Profile implements IProfile {
  constructor(
    public id?: number,
    public name?: string,
    public status?: Category,
    public specialite?: Filiere | null,
    public address?: string | null,
    public codeP?: string | null,
    public ville?: string | null,
    public phoneNumber?: string | null,
    public email?: string | null,
    public indicatifContentType?: string | null,
    public indicatif?: string | null,
    public user?: IUser | null,
    public works?: ITask[] | null,
    public documents?: IReport[] | null,
    public houses?: IGreenHouse[] | null
  ) {}
}

export function getProfileIdentifier(profile: IProfile): number | undefined {
  return profile.id;
}
