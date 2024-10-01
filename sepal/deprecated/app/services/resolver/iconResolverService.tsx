import {MdAccountCircle, MdDashboard, MdOutlineHistory} from "react-icons/md";
import {CgArrowsExchange} from "react-icons/cg";
import {FaNewspaper, FaRegChartBar} from "react-icons/fa";
import {IoIosMail} from "react-icons/io";
import {PiUserSwitchLight, PiWarningCircleBold} from "react-icons/pi";
import {RiLogoutCircleLine} from "react-icons/ri";
import {BsPatchExclamation} from "react-icons/bs";
import {VscAccount} from "react-icons/vsc";

/**
 * Resolves string ids into their respective icon component
 *
 * @param val icon id
 */
export function resolveIcon(val: string) {
  switch (val) {
    case 'MdAccountCircle':
      return <MdAccountCircle />
    case 'MdDashboard':
      return <MdDashboard />
    case 'CgArrowsExchange':
      return <CgArrowsExchange />
    case 'MdOutlineHistory':
      return <MdOutlineHistory />
    case 'FaNewspaper':
      return <FaNewspaper />
    case 'IoIosMail':
      return <IoIosMail />
    case 'PiWarningCircleBold':
      return <PiWarningCircleBold />
    case 'RiLogoutCircleLine':
      return <RiLogoutCircleLine />
    case 'PiUserSwitchLight':
      return <PiUserSwitchLight />
    case 'BsPatchExclamation':
      return <BsPatchExclamation />
    case 'VscAccount':
      return <VscAccount />
    case 'FaRegChartBar':
      return <FaRegChartBar />
        default:
      return null
  }
}