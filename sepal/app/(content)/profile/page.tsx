'use client'

import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import {useEffect, useState} from "react";
import {Icons} from "@/lib/enums";

/**
 * Renders the user profile page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ProfilePage() {

  const {
    pageTitle,
    pageSubtitle,
    pageIconCode,
    breadcrumbs,
    user,
    setPageTitle,
    setPageSubtitle,
    setPageIconCode,
    setBreadcrumbs,
    setUser
  } = useSepalPageInfoContext()

  const [isLoading, setIsLoading] = useState(false)

  useEffect(() => {
    setPageTitle('User Profile')
    setPageSubtitle(`A look at your profile.`)
    setPageIconCode(Icons.UserProfile)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'My Profile', href: '/profile', active: true},
    ])
  }, [])


  //  RENDER

  return (
    <div className={''}>
      My Profile Page
    </div>
  )
}