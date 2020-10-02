package com.hunorkovacs.itsaren

import zio.Has

package object simple {

  type Logging = Has[Logging.Service]

  type UserRepo = Has[UserRepo.Service]

}
