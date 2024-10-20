import { Component, Input, OnInit, Renderer2, ElementRef, Output, EventEmitter } from '@angular/core';
import { UserProfileWithRoleDTO } from '../../../../services/dto/forum/user/user-profile-with-role-dto.interface';
import { UserProfileService } from '../../../../services/user-profile.service';
import { ForumService } from '../../../../services/forum.service';

@Component({
  selector: 'app-user-item',
  templateUrl: './user-item.component.html',
  styleUrls: ['./user-item.component.css']
})
export class UserItemComponent implements OnInit {
  @Input() public user!: UserProfileWithRoleDTO;
  @Input() public isModerator!: boolean;
  @Input() public currentUserId!: number;
  @Input() public forumCreatorId!: number;
  @Input() public isCreator!: boolean;  // Whether the current user is the forum creator
  @Input() public isAdmin!: boolean;    // Whether the current user is an admin/moderator
  @Input() public forumId!: number;

  @Output() public adminAddedEvent = new EventEmitter<number>();

  public userProfilePhotoUrl!: string;
  public isDropdownOpen: boolean = false;

  constructor(private _userProfileService: UserProfileService,
    private _forumService: ForumService,
     private renderer: Renderer2,
      private el: ElementRef) {}

  ngOnInit(): void {
    if (this.user.profilePhoto) {
      this.loadUserProfilePhoto(this.user.profilePhoto.photoId);
    }

    // Close the dropdown if clicked outside
    this.renderer.listen('window', 'click', (event: Event) => {
      if (!this.el.nativeElement.contains(event.target)) {
        this.isDropdownOpen = false;
      }
    });
    console.log("SOY CREADOR, SOY ADMIN", this.isCreator, this.isAdmin);

  }

  toggleDropdown(event: MouseEvent): void {
    event.stopPropagation(); // Prevent bubbling
    this.isDropdownOpen = !this.isDropdownOpen;
    if (this.isDropdownOpen) {
      this.closeOtherDropdowns();
    }
  }

  closeOtherDropdowns(): void {
    const openDropdowns = document.querySelectorAll('.dropdown-menu');
    openDropdowns.forEach(dropdown => {
      if (dropdown !== this.el.nativeElement.querySelector('.dropdown-menu')) {
        dropdown.classList.add('hidden');
      }
    });
  }

  // Logic to determine if the "Banear Usuario" button should be enabled or disabled
  canBanUser(): boolean {
    if (this.isCreator) {
      return true; // Creator can ban anyone
    }

    if (this.isAdmin && this.user.userType !== 'Admin') {
      return true; // Moderators can ban subscribers, but not other admins
    }

    return false;
  }

  shouldShowBanButton(): boolean {
    // Show the ban button for Creator and Admins, but never for Subscribers
    return this.isCreator || this.isAdmin;
  }

    // Method to ban a user
    banUser(): void {
      if (this.canBanUser()) {
        this._forumService.banUser(this.forumCreatorId, this.currentUserId, this.user.userId).subscribe({
          next: () => {
            console.log(`User ${this.user.userId} banned successfully.`);
            // Optional: Update the UI to reflect the change
          },
          error: (err) => {
            console.error('Failed to ban user:', err);
          }
        });
      }
    }

  getUserTypeLabel(userType: string): string {
    switch (userType) {
      case 'Creator':
        return 'Creador';
      case 'Admin':
        return 'Moderador';
      case 'Subscriber':
      default:
        return 'Suscriptor';
    }
  }

  public addAdmin(){
    let userId: number = this.user.userId;
    this._forumService.addAdmin(this.forumId, userId).subscribe(() => {
      this.adminAddedEvent.emit(userId);
    },
    err => {
      console.log(err);
    })
  }

  private loadUserProfilePhoto(photoId: number) {
    this._userProfileService.getUserProfilePhoto(photoId).subscribe((resp: Blob) => {
      this.userProfilePhotoUrl = URL.createObjectURL(resp);
    }, err => {
      console.error('Failed to load user photo:', err);
    });
  }
}
